package com.fusioncrew.aikiosk.domain.cart.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.service.CartService;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    public ResponseEntity<CartDtos.CartResponse> get(@PathVariable String cartId) {
        return ResponseEntity.ok(toRes(cartService.get(cartId)));
    }

    @PostMapping("/carts/{cartId}/items")
    public ResponseEntity<CartDtos.CartResponse> add(
            @PathVariable String cartId,
            @RequestBody CartDtos.AddItemRequest req
    ) {
        return ResponseEntity.ok(toRes(cartService.addItem(cartId, req)));
    }

    @PatchMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<CartDtos.CartResponse> updateQty(
            @PathVariable String cartId,
            @PathVariable String itemId,
            @RequestBody CartDtos.UpdateQtyRequest req
    ) {
        int qty = (req.quantity() == null ? 1 : req.quantity());
        return ResponseEntity.ok(toRes(cartService.updateQty(cartId, itemId, qty)));
    }

    @DeleteMapping("/carts/{cartId}/items/{itemId}")
    public ResponseEntity<Map<String, Object>> deleteItem(
            @PathVariable String cartId,
            @PathVariable String itemId
    ) {
        cartService.deleteItem(cartId, itemId);

        Map<String, Object> data = new HashMap<>();
        data.put("deleted", true);
        data.put("cartId", cartId);
        data.put("itemId", itemId);

        return ResponseEntity.ok(commonResponse(data));
    }

    @DeleteMapping("/carts/{cartId}/items")
    public ResponseEntity<Map<String, Object>> clear(@PathVariable String cartId) {
        cartService.clear(cartId);

        Map<String, Object> data = new HashMap<>();
        data.put("cleared", true);
        data.put("cartId", cartId);

        return ResponseEntity.ok(commonResponse(data));
    }

    private CartDtos.CartResponse toRes(Cart cart) {
        return new CartDtos.CartResponse(
                CartDtos.formatCartId(cart.getId()),
                cart.getSessionId(),
                cart.getItems().stream()
                        .map(i -> new CartDtos.CartItemResponse(
                                CartDtos.formatCartItemId(i.getId()),
                                i.getMenuItemId(),
                                i.getQuantity(),
                                i.getOptionsJson()
                        ))
                        .collect(Collectors.toList()),
                cart.getCreatedAt(),
                cart.getUpdatedAt()
        );
    }

    private Map<String, Object> commonResponse(Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", data);
        res.put("timestamp", OffsetDateTime.now());
        res.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return res;
    }
}