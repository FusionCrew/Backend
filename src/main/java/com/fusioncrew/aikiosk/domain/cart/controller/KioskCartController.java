package com.fusioncrew.aikiosk.domain.cart.controller;

import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.service.CartService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/kiosk")
public class KioskCartController {

    private final CartService cartService;

    public KioskCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts")
    public ResponseEntity<ApiResponse<CartDtos.CartCreateResponse>> create(
            @RequestBody CartDtos.CreateCartRequest req) {
        Cart cart = cartService.createOrGet(req.sessionId());
        CartDtos.CartCreateResponse data = new CartDtos.CartCreateResponse(
                CartDtos.formatCartId(cart.getId()),
                cart.getSessionId(),
                cart.getStatus().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(data));
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<ApiResponse<CartDtos.CartGetResponse>> get(@PathVariable String cartId) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.getWithDetails(cartId)));
    }

    @PostMapping("/carts/{cartId}/items")
    public ResponseEntity<ApiResponse<CartDtos.CartItemAddResponse>> add(
            @PathVariable String cartId,
            @RequestBody CartDtos.AddItemRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(cartService.addItem(cartId, req)));
    }

    @PatchMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse<CartDtos.CartItemAddResponse>> updateQty(
            @PathVariable String cartId,
            @PathVariable String itemId,
            @RequestBody CartDtos.UpdateQtyRequest req) {
        int qty = (req.quantity() == null ? 1 : req.quantity());
        return ResponseEntity.ok(ApiResponse.ok(cartService.updateQty(cartId, itemId, qty)));
    }

    @DeleteMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteItem(
            @PathVariable String cartId,
            @PathVariable String itemId) {
        cartService.deleteItem(cartId, itemId);

        Map<String, Object> data = new HashMap<>();
        data.put("deleted", true);
        data.put("cartId", cartId);
        data.put("itemId", itemId);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    @DeleteMapping("/carts/{cartId}/items")
    public ResponseEntity<ApiResponse<Map<String, Object>>> clear(@PathVariable String cartId) {
        cartService.clear(cartId);

        Map<String, Object> data = new HashMap<>();
        data.put("cleared", true);
        data.put("cartId", cartId);

        return ResponseEntity.ok(ApiResponse.ok(data));
    }

    private CartDtos.CartResponse toRes(Cart cart) {
        return new CartDtos.CartResponse(
                CartDtos.formatCartId(cart.getId()),
                cart.getSessionId(),
                cart.getStatus().name(),
                cart.getItems().stream()
                        .map(i -> new CartDtos.CartItemResponse(
                                CartDtos.formatCartItemId(i.getId()),
                                i.getMenuItemId(),
                                i.getQuantity(),
                                i.getOptionsJson()))
                        .collect(Collectors.toList()),
                cart.getCreatedAt(),
                cart.getUpdatedAt());
    }
}