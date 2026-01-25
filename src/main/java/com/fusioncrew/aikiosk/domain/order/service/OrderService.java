package com.fusioncrew.aikiosk.domain.order.service;

import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.domain.cart.repository.CartRepository;
import com.fusioncrew.aikiosk.domain.order.dto.*;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
            MenuItemRepository menuItemRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @Transactional
    public Order createFromCart(OrderDtos.CreateOrderRequest req) {
        if (req.cartId() == null)
            throw new IllegalArgumentException("cartId is required");
        if (req.sessionId() == null || req.sessionId().isBlank())
            throw new IllegalArgumentException("sessionId is required");

        Cart cart = cartRepository.findById(req.cartId())
                .orElseThrow(() -> new IllegalArgumentException("cart not found"));

        if (cart.getItems().isEmpty())
            throw new IllegalArgumentException("cart is empty");

        Order order = new Order();
        order.setSessionId(req.sessionId());
        order.setStatus(OrderStatus.PENDING);

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            Long mId = ci.getMenuItemId();
            oi.setMenuItemId(mId);

            // 메뉴 상세 정보 조회하여 name, price 채우기 (mId는 MenuItem.id)
            menuItemRepository.findById(mId).ifPresent(menu -> {
                oi.setName(menu.getName());
                oi.setPrice(menu.getPrice());
            });

            oi.setQuantity(ci.getQuantity());
            oi.setOptionsJson(ci.getOptionsJson());
            order.addItem(oi);
        }

        Order saved = orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return saved;
    }

    public Order get(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("order not found"));
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

        Order saved = orderRepository.save(order);

        return OrderStatusUpdateResponseDto.from(saved, previousStatus);
    }

    private void validateStatusTransition(OrderStatus from, OrderStatus to) {
        // 이미 취소된 주문은 변경 불가
        if (from == OrderStatus.CANCELLED) {
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
        if (order.getStatus() == OrderStatus.CANCELLED)
            throw new IllegalArgumentException("cannot confirm cancelled order");
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = get(orderId);
        if (order.getStatus() == OrderStatus.CONFIRMED)
            throw new IllegalArgumentException("cannot cancel confirmed order");
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}