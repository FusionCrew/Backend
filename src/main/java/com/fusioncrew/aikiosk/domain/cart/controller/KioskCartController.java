package com.fusioncrew.aikiosk.domain.cart.controller;

import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kiosk")
public class KioskCartController {

    private final CartService cartService;

    public KioskCartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/carts")
    public ResponseEntity<CartDtos.CartResponse> create(@RequestBody CartDtos.CreateCartRequest req) {
        return ResponseEntity.ok(toRes(cartService.createOrGet(req.sessionId())));
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<CartDtos.CartResponse> get(@PathVariable Long cartId) {
        return ResponseEntity.ok(toRes(cartService.get(cartId)));
    }

    @PostMapping("/carts/{cartId}/items")
    public ResponseEntity<CartDtos.CartResponse> add(@PathVariable Long cartId,
            @RequestBody CartDtos.AddItemRequest req) {
        return ResponseEntity.ok(toRes(cartService.addItem(cartId, req)));
    }

    @PatchMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<CartDtos.CartResponse> updateQty(@PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestBody CartDtos.UpdateQtyRequest req) {
        int qty = (req.quantity() == null ? 1 : req.quantity());
        return ResponseEntity.ok(toRes(cartService.updateQty(cartId, itemId, qty)));
    }

    @DeleteMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<CartDtos.CartResponse> deleteItem(@PathVariable Long cartId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(toRes(cartService.deleteItem(cartId, itemId)));
    }

    @DeleteMapping("/carts/{cartId}/items")
    public ResponseEntity<CartDtos.CartResponse> clear(@PathVariable Long cartId) {
        return ResponseEntity.ok(toRes(cartService.clear(cartId)));
    }

    private CartDtos.CartResponse toRes(Cart cart) {
        return new CartDtos.CartResponse(
                cart.getId(),
                cart.getSessionId(),
                cart.getItems().stream()
                        .map(i -> new CartDtos.CartItemResponse(i.getId(), i.getMenuItemId(), i.getQuantity(),
                                i.getOptionsJson()))
                        .collect(Collectors.toList()),
                cart.getCreatedAt(),
                cart.getUpdatedAt());
    }
}