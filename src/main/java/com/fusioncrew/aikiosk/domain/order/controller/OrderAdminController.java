package com.fusioncrew.aikiosk.domain.order.controller;

import com.fusioncrew.aikiosk.domain.order.dto.OrderDetailResponseDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderResponseDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderStatusUpdateRequestDto;
import com.fusioncrew.aikiosk.domain.order.dto.OrderStatusUpdateResponseDto;
import com.fusioncrew.aikiosk.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderAdminController {

    private final OrderService orderService;

    @GetMapping
    public Map<String, Object> getOrderList() {
        List<OrderResponseDto> orders = orderService.getOrderList();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", Map.of("items", orders));
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    @GetMapping("/{orderId}")
    public Map<String, Object> getOrderDetail(@PathVariable String orderId) {
        OrderDetailResponseDto orderDetail = orderService.getOrderDetail(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", orderDetail);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }

    @PostMapping("/{orderId}/status")
    public Map<String, Object> updateOrderStatus(
            @PathVariable String orderId,
            @RequestBody OrderStatusUpdateRequestDto request) {

        OrderStatusUpdateResponseDto responseDto = orderService.updateOrderStatus(orderId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", responseDto);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));

        return response;
    }
}