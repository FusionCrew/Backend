package com.fusioncrew.aikiosk.domain.cart.dto;

import java.util.Map;
import java.util.List;
import java.time.OffsetDateTime;

public class CartDtos {

    public record CreateCartRequest(String sessionId) {
    }

    public record AddItemRequest(String menuItemId, Integer quantity, String optionsJson) {
    }

    public record UpdateQtyRequest(Integer quantity) {
    }

    public record CartItemResponse(String itemId, String menuItemId, int quantity, String optionsJson) {
    }

    public record CartResponse(String cartId, String sessionId, List<CartItemResponse> items,
            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    }

    public static String formatCartId(Long id) {
        return String.format("cart_%04d", id);
    }

    public static String formatCartItemId(Long id) {
        return String.format("ci_%02d", id);
    }

    public static Long parseCartId(String cartId) {
        if (cartId == null)
            throw new IllegalArgumentException("cartId is required");
        return Long.parseLong(cartId.replace("cart_", "").trim());
    }

    public static Long parseCartItemId(String itemId) {
        if (itemId == null)
            throw new IllegalArgumentException("itemId is required");
        return Long.parseLong(itemId.replace("ci_", "").trim());
    }
}