package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusUpdateResponseDto(
        Long orderId,
        OrderStatus status,
        LocalDateTime updatedAt) {
    public static OrderStatusUpdateResponseDto from(Order order) {
        return new OrderStatusUpdateResponseDto(order.getId(), order.getStatus(), order.getUpdatedAt());
    }
}