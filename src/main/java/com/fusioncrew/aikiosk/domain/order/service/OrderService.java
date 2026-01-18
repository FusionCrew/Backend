package com.fusioncrew.aikiosk.domain.order.service;

import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.domain.cart.repository.CartRepository;
import com.fusioncrew.aikiosk.domain.order.dto.*;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Order createFromCart(OrderDtos.CreateOrderRequest req) {
        if (req.cartId() == null) throw new IllegalArgumentException("cartId is required");
        if (req.sessionId() == null || req.sessionId().isBlank()) throw new IllegalArgumentException("sessionId is required");

        Cart cart = cartRepository.findById(req.cartId())
                .orElseThrow(() -> new IllegalArgumentException("cart not found"));

        if (cart.getItems().isEmpty()) throw new IllegalArgumentException("cart is empty");

        Order order = new Order();
        order.setSessionId(req.sessionId());
        order.setStatus(OrderStatus.PENDING);

        for (CartItem ci : cart.getItems()) {
            OrderItem oi = new OrderItem();
            oi.setMenuItemId(ci.getMenuItemId());
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
                .sorted(Comparator.comparing(Order::getCreatedAt).reversed())
                .map(OrderResponseDto::from)
                .toList();
    }

    public OrderDetailResponseDto getOrderDetail(String orderId) {
        Long id = parseOrderId(orderId);
        Order order = get(id);
        return OrderDetailResponseDto.from(order);
    }

    @Transactional
    public OrderStatusUpdateResponseDto updateOrderStatus(String orderId, OrderStatusUpdateRequestDto request) {
        Long id = parseOrderId(orderId);
        Order order = get(id);

        if (request == null || request.status() == null) {
            throw new IllegalArgumentException("status is required");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("cannot update cancelled order");
        }

        order.setStatus(request.status());
        Order saved = orderRepository.save(order);

        return OrderStatusUpdateResponseDto.from(saved);
    }

    private Long parseOrderId(String orderId) {
        try {
            return Long.parseLong(orderId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("orderId must be a number (Order.id)");
        }
    }

    @Transactional
    public Order confirm(Long orderId) {
        Order order = get(orderId);
        if (order.getStatus() == OrderStatus.CANCELLED) throw new IllegalArgumentException("cannot confirm cancelled order");
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancel(Long orderId) {
        Order order = get(orderId);
        if (order.getStatus() == OrderStatus.CONFIRMED) throw new IllegalArgumentException("cannot cancel confirmed order");
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }
}