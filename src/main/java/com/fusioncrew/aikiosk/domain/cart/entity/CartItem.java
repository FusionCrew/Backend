package com.fusioncrew.aikiosk.domain.cart.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(nullable = false)
    private Long menuItemId;

    @Column(nullable = false)
    private int quantity;

    @Lob
    private String optionsJson;

    public Long getId() { return id; }
    public Cart getCart() { return cart; }
    public Long getMenuItemId() { return menuItemId; }
    public int getQuantity() { return quantity; }
    public String getOptionsJson() { return optionsJson; }

    public void setCart(Cart cart) { this.cart = cart; }
    public void setMenuItemId(Long menuItemId) { this.menuItemId = menuItemId; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setOptionsJson(String optionsJson) { this.optionsJson = optionsJson; }
}