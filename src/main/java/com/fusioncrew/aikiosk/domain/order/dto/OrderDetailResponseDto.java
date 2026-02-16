package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        String orderId,
        Integer orderNumber,
        String sessionId,
        OrderStatus status,
        int totalPrice,
        LocalDateTime createdAt,
        String note,
        PaymentDto payment,
        List<Item> items) {

    public record Item(String menuItemId, String name, int price, int quantity, String optionsJson) {
        public static Item from(OrderItem oi) {
            return new Item(
                    oi.getMenuItemId(),
                    oi.getName(),
                    oi.getUnitPrice(),
                    oi.getQuantity(),
                    oi.getOptionsJson());
        }
    }

    public record PaymentDto(String method, String status) {
    }

    public static OrderDetailResponseDto from(Order order) {
        return new OrderDetailResponseDto(
                order.getOrderId(),
                order.getOrderNumber(),
                order.getSessionId(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                order.getStatusUpdateNote(),
                new PaymentDto("MOCK", "PAID"), // ?꾩옱??Mock ?곗씠??諛섑솚
                order.getItems().stream().map(Item::from).toList());
    }
}
