package com.fusioncrew.aikiosk.domain.order.entity;

import com.fusioncrew.aikiosk.global.exception.InvalidOrderStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    // 유효한 상태 전이 정의
    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.CREATED, Set.of(OrderStatus.PENDING, OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.PENDING, Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
            OrderStatus.CONFIRMED, Set.of(OrderStatus.MAKING, OrderStatus.CANCELLED),
            OrderStatus.MAKING, Set.of(OrderStatus.READY, OrderStatus.CANCELLED),
            OrderStatus.READY, Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED),
            OrderStatus.COMPLETED, Set.of(),
            OrderStatus.CANCELLED, Set.of());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private String sessionId;

    @Column
    private String note;

    @Column
    private LocalDateTime statusUpdatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order(String orderId, int totalPrice, String sessionId) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.sessionId = sessionId;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
    }

    public OrderStatus updateStatus(OrderStatus newStatus) {
        // 초기화 용도로 검증 없이 업데이트 (테스트 데이터 등)
        this.status = newStatus;
        this.statusUpdatedAt = LocalDateTime.now();
        return this.status;
    }

    public OrderStatus updateStatusWithValidation(OrderStatus newStatus, String note) {
        OrderStatus previousStatus = this.status;

        // 상태 전이 검증
        Set<OrderStatus> validNextStatuses = VALID_TRANSITIONS.get(this.status);
        if (validNextStatuses == null || !validNextStatuses.contains(newStatus)) {
            throw new InvalidOrderStatusException(
                    "현재 상태에서 변경할 수 없는 주문 상태입니다. 현재: " + this.status + ", 요청: " + newStatus);
        }

        this.status = newStatus;
        this.note = note;
        this.statusUpdatedAt = LocalDateTime.now();
        return previousStatus;
    }

    public void addOrderItem(OrderItem item) {
        this.items.add(item);
    }
}