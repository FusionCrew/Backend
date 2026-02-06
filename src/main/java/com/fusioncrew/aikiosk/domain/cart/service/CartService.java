package com.fusioncrew.aikiosk.domain.cart.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusioncrew.aikiosk.domain.cart.dto.CartDtos;
import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import com.fusioncrew.aikiosk.domain.cart.entity.CartItem;
import com.fusioncrew.aikiosk.domain.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Transactional
    public Cart createOrGet(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) throw new IllegalArgumentException("sessionId is required");
        return cartRepository.findBySessionId(sessionId).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setSessionId(sessionId);
            return cartRepository.save(cart);
        });
    }

    public Cart get(String cartId) {
        Long id = CartDtos.parseCartId(cartId);
        return cartRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("cart not found"));
    }

    @Transactional
    public Cart addItem(String cartId, CartDtos.AddItemRequest req) {
        Cart cart = get(cartId);
        if (req.menuItemId() == null || req.menuItemId().isBlank())
            throw new IllegalArgumentException("menuItemId is required");

        int qty = (req.quantity() == null ? 1 : req.quantity());
        if (qty <= 0) throw new IllegalArgumentException("quantity must be > 0");

        CartItem item = new CartItem();
        item.setMenuItemId(req.menuItemId());
        item.setQuantity(qty);
        item.setOptionsJson(req.optionsJson());
        cart.addItem(item);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQty(String cartId, String itemId, int qty) {
        if (qty <= 0) throw new IllegalArgumentException("quantity must be > 0");
        Cart cart = get(cartId);

        Long targetId = CartDtos.parseCartItemId(itemId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(targetId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("item not found"));

        item.setQuantity(qty);
        return cartRepository.save(cart);
    }

    @Transactional
    public void deleteItem(String cartId, String itemId) {
        Cart cart = get(cartId);
        Long targetId = CartDtos.parseCartItemId(itemId);
        cart.getItems().removeIf(i -> i.getId().equals(targetId));
        cartRepository.save(cart);
    }

    @Transactional
    public void clear(String cartId) {
        Cart cart = get(cartId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}