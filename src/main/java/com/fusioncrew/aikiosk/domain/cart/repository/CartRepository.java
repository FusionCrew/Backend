package com.fusioncrew.aikiosk.domain.cart.repository;

import com.fusioncrew.aikiosk.domain.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findBySessionId(String sessionId);
}