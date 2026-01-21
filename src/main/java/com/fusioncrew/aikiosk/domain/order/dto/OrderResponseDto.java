package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;

public record OrderResponseDto(
        Long orderId,
        String sessionId,
        OrderStatus status,
        int totalPrice,
        LocalDateTime createdAt) {
    public static OrderResponseDto from(Order order) {
        return new OrderResponseDto(
                order.getId(),
                order.getSessionId(),
                order.getStatus(),
                0, // 현재 Order에 totalPrice가 없으므로 일단 0 (추후 계산 로직 추가 가능)
                order.getCreatedAt());
    }
}