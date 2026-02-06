package com.fusioncrew.aikiosk.domain.order.controller;

import com.fusioncrew.aikiosk.domain.order.dto.OrderDtos;
import com.fusioncrew.aikiosk.domain.order.service.OrderService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/kiosk/orders")
public class KioskOrderController {

    private final OrderService orderService;

    public KioskOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDtos.OrderCreateResponse>> createOrder(
            @RequestBody OrderDtos.CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(orderService.createFromCart(request)));
    }

    @org.springframework.web.bind.annotation.GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDtos.OrderGetResponse>> getOrder(
            @org.springframework.web.bind.annotation.PathVariable String orderId) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getWithDetails(orderId)));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDtos.OrderCancelResponse>> cancelOrder(
            @org.springframework.web.bind.annotation.PathVariable String orderId,
            @RequestBody OrderDtos.OrderCancelRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.cancelOrder(orderId, request)));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderDtos.OrderConfirmResponse>> confirmOrder(
            @org.springframework.web.bind.annotation.PathVariable String orderId,
            @RequestBody OrderDtos.OrderConfirmRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.confirmOrder(orderId, request)));
    }
}
