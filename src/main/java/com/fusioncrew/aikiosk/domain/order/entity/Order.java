package com.fusioncrew.aikiosk.domain.order.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sessionId; // kiosk 세션 식별

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    void preUpdate() { this.updatedAt = OffsetDateTime.now(); }

    public Long getId() { return id; }
    public String getSessionId() { return sessionId; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return items; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }
}