package com.fusioncrew.aikiosk.domain.order.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Long menuItemId;

    @Column(nullable = false)
    private int quantity;

    @Lob
    private String optionsJson;

    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public Long getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public String getOptionsJson() { return optionsJson; }

    public void setOrder(Order order) { this.order = order; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setOptionsJson(String optionsJson) { this.optionsJson = optionsJson; }
}