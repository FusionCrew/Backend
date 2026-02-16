package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponseDto(
        String orderId,
        Integer orderNumber,
        String sessionId,
        OrderStatus status,
        int totalPrice,
        LocalDateTime createdAt) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getOrderId(),
                order.getOrderNumber(),
                order.getSessionId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt());
    }
}
