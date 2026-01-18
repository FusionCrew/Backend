package com.fusioncrew.aikiosk.domain.order.dto;

import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import java.time.OffsetDateTime;
import java.util.List;

public class OrderDtos {

    public record CreateOrderRequest(Long cartId, String sessionId) {}

    public record OrderItemResponse(Long itemId, Long menuItemId, int quantity, String optionsJson) {}

    public record OrderResponse(
            Long orderId,
            String sessionId,
            OrderStatus status,
            List<OrderItemResponse> items,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt
    ) {}
}