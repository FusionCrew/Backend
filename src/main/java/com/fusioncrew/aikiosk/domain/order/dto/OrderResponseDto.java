package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private String orderId;
    private OrderStatus status;
    private int totalPrice;
    private LocalDateTime createdAt;

    public static OrderResponseDto from(Order order) {
        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
