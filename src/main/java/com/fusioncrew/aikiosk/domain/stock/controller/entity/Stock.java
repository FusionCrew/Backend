package com.fusioncrew.aikiosk.domain.stock.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(
        name = "stocks",
        uniqueConstraints = @UniqueConstraint(name = "uk_stocks_ingredient_id", columnNames = "ingredient_id")
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_id", nullable = false, length = 80)
    private String ingredientId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private boolean outOfStock;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    protected Stock() {}

    public Stock(String ingredientId, int quantity) {
        this.ingredientId = ingredientId;
        setQuantity(quantity);
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() { return id; }
    public String getIngredientId() { return ingredientId; }
    public int getQuantity() { return quantity; }
    public boolean isOutOfStock() { return outOfStock; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }

    public void setQuantity(int quantity) {
        this.quantity = Math.max(quantity, 0);
        this.outOfStock = (this.quantity == 0);
    }

    public void applyDelta(int delta) {
        setQuantity(this.quantity + delta);
    }

    public void markOutOfStock() {
        this.quantity = 0;
        this.outOfStock = true;
    }
}