package com.fusioncrew.aikiosk.domain.order.entity;

import com.fusioncrew.aikiosk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String sessionId; // kiosk 세션 식별

    @Column(nullable = false, unique = true)
    private String orderId; // 주문 고유 식별자

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.CREATED;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderType orderType = OrderType.DINE_IN; // 기본값

    @Column(length = 255)
    private String memo; // 요청 사항

    @Column(nullable = false)
    @Builder.Default
    private int totalPrice = 0; // 주문 총 금액

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private OrderCancelReason cancelReason; // 주문 취소 사유

    @Column(length = 255)
    private String statusUpdateNote; // 관리자 상태 변경 메모

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.orderId == null) {
            this.orderId = "ord_" + java.util.UUID.randomUUID().toString().substring(0, 8);
        }
        if (this.sessionId == null) {
            this.sessionId = "sess_unknown";
        }
    }

    public void addOrderItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    public void addItem(OrderItem item) {
        addOrderItem(item);
    }
}
