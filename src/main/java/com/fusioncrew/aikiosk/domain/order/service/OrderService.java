package com.fusioncrew.aikiosk.domain.order.service;

import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.domain.cart.repository.CartRepository;
import com.fusioncrew.aikiosk.domain.order.dto.*;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderCancelReason;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.entity.OrderType;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import com.fusioncrew.aikiosk.domain.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;
    private final StockRepository stockRepository;
    private final com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository paymentRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
            MenuItemRepository menuItemRepository, StockRepository stockRepository,
            com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
        this.stockRepository = stockRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public OrderDtos.OrderCreateResponse createFromCart(OrderDtos.CreateOrderRequest req) {
        // 1. Validate inputs
        if (req.cartId() == null)
            throw new IllegalArgumentException("cartId is required");
        if (req.sessionId() == null || req.sessionId().isBlank())
            throw new IllegalArgumentException("sessionId is required");
        if (req.orderType() == null)
            throw new IllegalArgumentException("orderType is required");

        OrderType orderType;
        try {
            orderType = OrderType.valueOf(req.orderType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid orderType: " + req.orderType());
        }

        Long cartId = CartDtos.parseCartId(req.cartId());

        // 2. Fetch Cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("cart not found"));

        if (cart.getItems().isEmpty())
            throw new IllegalArgumentException("cart is empty");

        // 3. Create Order
        Order order = new Order();
        order.setSessionId(req.sessionId());
        order.setStatus(OrderStatus.CREATED);
        order.setOrderType(orderType);
        order.setMemo(req.memo());

        int totalPrice = 0;
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            String logicId = ci.getMenuItemId(); // menuItemId from CartItem is String

            // 메뉴 상세 정보 조회
            var menuItemOpt = menuItemRepository.findByMenuItemId(logicId);

            String menuItemIdForOrder = menuItemOpt
                    .map(com.fusioncrew.aikiosk.domain.menu.entity.MenuItem::getMenuItemId).orElse(logicId); // Use
                                                                                                             // logic ID
                                                                                                             // (String)
            String name = menuItemOpt.map(com.fusioncrew.aikiosk.domain.menu.entity.MenuItem::getName)
                    .orElse("Unknown");
            int basePrice = menuItemOpt.map(com.fusioncrew.aikiosk.domain.menu.entity.MenuItem::getPrice).orElse(0);

            oi.setMenuItemId(menuItemIdForOrder);
            oi.setName(name);

            // Calculate Option Price
            int optionPrice = 0;
            try {
                if (ci.getOptionsJson() != null && !ci.getOptionsJson().isBlank()) {
                    java.util.Map<String, Object> options = objectMapper.readValue(ci.getOptionsJson(),
                            new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {
                            });

                    // 1. Check for legacy/special flags
                    if (options.containsKey("isLargeSet") && Boolean.TRUE.equals(options.get("isLargeSet"))) {
                        optionPrice += 500;
                    }
                    if (options.containsKey("size") && "세트".equals(options.get("size"))) {
                        optionPrice += 3000;
                    }

                    // 2. Dynamic selectedOptions processing
                    if (options.get("selectedOptions") instanceof List<?> selectedOptions) {
                        for (Object optObj : selectedOptions) {
                            if (optObj instanceof java.util.Map<?, ?> optMap) {
                                Object extraPriceObj = optMap.get("extraPrice");
                                if (extraPriceObj instanceof Number num) {
                                    optionPrice += num.intValue();
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                // ignore parsing error
            }

            int finalUnitPrice = basePrice + optionPrice;
            oi.setUnitPrice(finalUnitPrice); // Set Unit Price matching the spec (includes options)

            int lineTotal = finalUnitPrice * ci.getQuantity();
            oi.setLineTotal(lineTotal); // Quantity * UnitPrice
            totalPrice += lineTotal;

            oi.setQuantity(ci.getQuantity());
            oi.setOptionsJson(ci.getOptionsJson());
            order.addItem(oi);
        }

        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);

        // 4. Clear Cart
        cart.getItems().clear();
        cartRepository.save(cart);

        return new OrderDtos.OrderCreateResponse(
                saved.getOrderId(),
                saved.getStatus().name(),
                new OrderDtos.Amount(saved.getTotalPrice(), "KRW"));
    }

    public OrderDtos.OrderGetResponse getWithDetails(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        List<OrderDtos.OrderItemDetailResponse> items = order.getItems().stream()
                .map(i -> new OrderDtos.OrderItemDetailResponse(
                        "oi_" + i.getId(), // 임시 ID 포맷
                        i.getMenuItemId(), // String
                        i.getName(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()))
                .toList();

        return new OrderDtos.OrderGetResponse(
                order.getOrderId(),
                order.getStatus().name(),
                items,
                new OrderDtos.Amount(order.getTotalPrice(), "KRW"));
    }

    public Order get(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
    }

    @Transactional
    public OrderDtos.OrderCancelResponse cancelOrder(String orderId, OrderDtos.OrderCancelRequest req) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        // 이미 취소된 주문이거나, 완료/픽업대기 상태 등 취소 불가능한 상태 체크 필요
        // 명세에는 단순히 취소 처리하라고 되어 있으나, 일반적인 비즈니스 로직상 완료된 주문은 취소(현장환불 등) 절차가 다르지만 여기서는 단순
        // 상태 변경으로 처리
        // 다만 COMPLETED 상태라면 취소가 안되어야 하는게 일반적임.
        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Order is already cancelled");
        }

        OrderCancelReason reason;
        try {
            reason = OrderCancelReason.valueOf(req.reason());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid cancel reason: " + req.reason());
        }

        order.setStatus(OrderStatus.CANCELED);
        order.setCancelReason(reason);

        // Payment Refund Logic
        paymentRepository.findByOrderId(orderId).ifPresent(payment -> {
            payment.refund(payment.getAmount()); // Full refund
            // payment is managed entity, so dirty checking will save it
        });

        return new OrderDtos.OrderCancelResponse(order.getOrderId(), order.getStatus().name());
    }

    @Transactional
    public OrderDtos.OrderConfirmResponse confirmOrder(String orderId, OrderDtos.OrderConfirmRequest req) {
        if (!req.agreeToPolicy()) {
            throw new IllegalArgumentException("Policy agreement is required.");
        }

        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalStateException(
                    "Order must be in CREATED state to confirm. Current state: " + order.getStatus());
        }

        // 재고 차감 로직: 주문 아이템 -> 메뉴 -> 재료 -> 재고
        System.out.println("[OrderService] Confirming order: " + orderId);
        for (OrderItem item : order.getItems()) {
            System.out.println("[OrderService] Processing item: " + item.getMenuItemId() + " / " + item.getName());
            menuItemRepository.findByMenuItemId(item.getMenuItemId()).ifPresentOrElse(menuItem -> {
                System.out.println("[OrderService] Found MenuItem: " + menuItem.getName() + " (Ingredients: "
                        + menuItem.getIngredients().size() + ")");
                for (var ingredient : menuItem.getIngredients()) {
                    System.out.println("[OrderService]   - Ingredient: " + ingredient.getName() + " ("
                            + ingredient.getIngredientId() + ")");
                    stockRepository.findByIngredientId(ingredient.getIngredientId()).ifPresentOrElse(stock -> {
                        int oldQty = stock.getQuantity();
                        stock.applyDelta(-item.getQuantity()); // 주문 수량만큼 차감
                        stockRepository.save(stock);
                        System.out.println("[OrderService]     -> Stock Updated: " + ingredient.getName() + " " + oldQty
                                + " -> " + stock.getQuantity());
                    }, () -> {
                        System.err.println("[OrderService]     -> Stock NOT FOUND for ingredient: "
                                + ingredient.getIngredientId());
                    });
                }
            }, () -> {
                System.err.println("[OrderService] MenuItem NOT FOUND for ID: " + item.getMenuItemId());
            });
        }

        order.setStatus(OrderStatus.CONFIRMED);

        return new OrderDtos.OrderConfirmResponse(order.getOrderId(), order.getStatus().name());
    }

    public List<OrderResponseDto> getOrderList() {
        return orderRepository.findAll().stream()
                .sorted(Comparator.comparing(Order::getId))
                .map(OrderResponseDto::from)
                .toList();
    }

    public OrderDetailResponseDto getOrderDetail(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderStatusUpdateResponseDto updateOrderStatus(String orderId, OrderStatusUpdateRequestDto request) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found: " + orderId));

        if (request == null || request.status() == null) {
            throw new IllegalArgumentException("status is required");
        }

        OrderStatus previousStatus = order.getStatus();
        OrderStatus newStatus = request.status();

        // 1. 상태 전이 유효성 검사
        validateStatusTransition(previousStatus, newStatus);

        // 2. 값 업데이트
        order.setStatus(newStatus);
        if (request.note() != null) {
            order.setStatusUpdateNote(request.note());
        }

        // 3. 주문 취소 시 환불 처리
        if (newStatus == OrderStatus.CANCELED) {
            paymentRepository.findByOrderId(order.getOrderId()).ifPresent(payment -> {
                payment.refund(payment.getAmount());
            });
        }

        Order saved = orderRepository.save(order);

        return OrderStatusUpdateResponseDto.from(saved, previousStatus);
    }

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        // 이미 취소된 주문은 변경 불가
        if (from == OrderStatus.CANCELED) {
            throw new IllegalArgumentException("이미 취소된 주문은 상태를 변경할 수 없습니다.");
        }

        // 동일한 상태로의 변경은 허용 (Idempotent)
        if (from == to)
            return;

        // 역방향 전이 금지 예시 (READY -> MAKING 등)
        if (from == OrderStatus.READY && to == OrderStatus.MAKING) {
            throw new IllegalArgumentException("준비 완료된 주문은 다시 조리 중 상태로 변경할 수 없습니다.");
        }

        if (from == OrderStatus.COMPLETED) {
            throw new IllegalArgumentException("이미 완료된 주문은 상태를 변경할 수 없습니다.");
        }

        // 여기에 추가적인 비즈니스 룰 정의 가능
    }

    @Transactional
    public Order confirm(Long orderId) {
        Order order = get(orderId);
        if (order.getStatus() == OrderStatus.CANCELED)
            throw new IllegalArgumentException("cannot confirm cancelled order");
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = get(orderId);
        if (order.getStatus() == OrderStatus.CONFIRMED)
            throw new IllegalArgumentException("cannot cancel confirmed order");
        order.setStatus(OrderStatus.CANCELED);

        // Payment sync
        paymentRepository.findByOrderId(order.getOrderId()).ifPresent(payment -> {
            payment.refund(payment.getAmount());
        });

        return orderRepository.save(order);
    }
}