package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderStatusUpdateResponseDto(
        String orderId,
        OrderStatus previousStatus,
        OrderStatus currentStatus,
        String note,
        UpdatedByDto updatedBy,
        LocalDateTime updatedAt) {

    public record UpdatedByDto(String adminUserId, String username) {
    }

    public static OrderStatusUpdateResponseDto from(Order order, OrderStatus previousStatus) {
        return new OrderStatusUpdateResponseDto(
                order.getOrderId(),
                previousStatus,
                order.getStatus(),
                order.getStatusUpdateNote(),
                new UpdatedByDto("adm_0001", "admin01"), // Mock
                order.getUpdatedAt());
    }
}