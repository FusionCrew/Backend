package com.fusioncrew.aikiosk.domain.order.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuItemId; // 메뉴 ID (String)

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int unitPrice; // 단가

    @Column(nullable = false)
    private int quantity; // 수량

    @Column(nullable = false)
    private int lineTotal; // 총 금액 (단가+옵션 * 수량)

    @Column(columnDefinition = "TEXT")
    private String optionsJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_order")
    private Order order;

    @PrePersist
    public void prePersist() {
        if (this.name == null) {
            this.name = "Unknown Item";
        }
        if (this.menuItemId == null) {
            this.menuItemId = "unknown";
        }
    }
}
