package com.fusioncrew.aikiosk.domain.order.controller;

import com.fusioncrew.aikiosk.domain.order.dto.OrderDtos;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kiosk")
public class KioskOrderController {

    private final OrderService orderService;

    public KioskOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDtos.OrderResponse> create(@RequestBody OrderDtos.CreateOrderRequest req) {
        return ResponseEntity.ok(toRes(orderService.createFromCart(req)));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDtos.OrderResponse> get(@PathVariable Long orderId) {
        return ResponseEntity.ok(toRes(orderService.get(orderId)));
    }

    @PostMapping("/orders/{orderId}/confirm")
    public ResponseEntity<OrderDtos.OrderResponse> confirm(@PathVariable Long orderId) {
        return ResponseEntity.ok(toRes(orderService.confirm(orderId)));
    }

    @PostMapping("/orders/{orderId}/cancel")
    public ResponseEntity<OrderDtos.OrderResponse> cancel(@PathVariable Long orderId) {
        return ResponseEntity.ok(toRes(orderService.cancel(orderId)));
    }

    private OrderDtos.OrderResponse toRes(Order order) {
        return new OrderDtos.OrderResponse(
                order.getId(),
                order.getSessionId(),
                order.getStatus(),
                order.getItems().stream()
                        .map(i -> new OrderDtos.OrderItemResponse(i.getId(), i.getMenuItemId(), i.getQuantity(), i.getOptionsJson()))
                        .collect(Collectors.toList()),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}