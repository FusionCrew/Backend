package com.fusioncrew.aikiosk.domain.cart.dto;

import java.time.OffsetDateTime;
import java.util.List;

public class CartDtos {
    public record CreateCartRequest(String sessionId) {}
    public record AddItemRequest(Long menuItemId, Integer quantity, String optionsJson) {}
    public record UpdateQtyRequest(Integer quantity) {}
    public record CartItemResponse(Long itemId, Long menuItemId, int quantity, String optionsJson) {}
    public record CartResponse(Long cartId, String sessionId, List<CartItemResponse> items,
                               OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
}