package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        String orderId,
        String sessionId,
        OrderStatus status,
        int totalPrice,
        LocalDateTime createdAt,
        String note,
        PaymentDto payment,
        List<Item> items) {

    public record Item(String menuItemId, String name, int price, int quantity) {
        public static Item from(OrderItem oi) {
            return new Item(
                    String.valueOf(oi.getMenuItemId()),
                    oi.getName(),
                    oi.getPrice(),
                    oi.getQuantity());
        }
    }

    public record PaymentDto(String method, String status) {
    }

    public static OrderDetailResponseDto from(Order order) {
        int total = order.getItems().stream()
                .mapToInt(item -> item.getPrice() * item.getQuantity())
                .sum();

        return new OrderDetailResponseDto(
                order.getOrderId(),
                order.getSessionId(),
                order.getStatus(),
                total,
                order.getCreatedAt(),
                order.getStatusUpdateNote(),
                new PaymentDto("MOCK", "PAID"), // 현재는 Mock 데이터 반환
                order.getItems().stream().map(Item::from).toList());
    }
}