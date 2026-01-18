package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderItem;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;

public record OrderDetailResponseDto(
        Long orderId,
        String sessionId,
        OrderStatus status,
        int totalPrice,
        OffsetDateTime createdAt,
        List<Item> items
) {
    public record Item(Long menuItemId, int quantity, String optionsJson) {
        public static Item from(OrderItem oi) {
            return new Item(oi.getMenuItemId(), oi.getQuantity(), oi.getOptionsJson());
        }
    }

    public static OrderDetailResponseDto from(Order order) {
        return new OrderDetailResponseDto(
                order.getId(),
                order.getSessionId(),
                order.getStatus(),
                0, // ✅ totalPrice 아직 없으니 0
                order.getCreatedAt(),
                order.getItems().stream().map(Item::from).toList()
        );
    }
}