package com.fusioncrew.aikiosk.domain.cart.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts", indexes = {
        @Index(name = "idx_carts_session_id", columnList = "sessionId", unique = true)
})
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sessionId; // 세션당 1개 권장

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<CartItem> items = new ArrayList<>();

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    void preUpdate() { this.updatedAt = OffsetDateTime.now(); }

    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public List<CartItem> getItems() { return items; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public void addItem(CartItem item) {
        item.setCart(this);
        this.items.add(item);
    }
}