package com.fusioncrew.aikiosk.domain.cart.dto;

import java.util.Map;
import java.util.List;
import java.time.OffsetDateTime;

public class CartDtos {

    // === Request DTOs ===

    public record CreateCartRequest(String sessionId) {
    }

    // 장바구니 아이템 추가 요청 - 명세 기준
    public record AddItemRequest(String menuItemId, Integer quantity, Map<String, Object> options) {
    }

    public record UpdateQtyRequest(Integer quantity) {
    }

    // === Response DTOs ===

    // 장바구니 생성 응답 (POST /carts)
    public record CartCreateResponse(String cartId, String sessionId, String status) {
    }

    // 장바구니 조회 응답 (GET /carts/{cartId}) - 명세 기준
    public record CartGetResponse(
            String cartId,
            List<CartItemDetailResponse> items,
            int totalPrice) {
    }

    // 장바구니 아이템 상세 응답 - 명세 기준
    public record CartItemDetailResponse(
            String itemId,
            String menuItemId,
            String menuCode,
            String name,
            int quantity,
            int unitPrice,
            Map<String, Object> options,
            int lineTotal) {
    }

    // 장바구니 아이템 추가 응답 (POST /carts/{cartId}/items) - 명세 기준
    public record CartItemAddResponse(
            String itemId,
            String cartId,
            String menuItemId,
            String menuCode,
            String name,
            int quantity,
            int unitPrice,
            Map<String, Object> options,
            int lineTotal) {
    }

    // 기존 CartResponse (수량 변경 시 반환용)
    public record CartItemResponse(String itemId, String menuItemId, int quantity, String optionsJson) {
    }

    public record CartResponse(String cartId, String sessionId, String status, List<CartItemResponse> items,
            OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    }

    // === Utility Methods ===

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
