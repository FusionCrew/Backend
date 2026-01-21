package com.fusioncrew.aikiosk.domain.payment.entity;

import com.fusioncrew.aikiosk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false)
    private boolean mock;

    @Column(nullable = false)
    private String returnUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private String pgTid;
    private String pgProvider;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal refundedAmount = BigDecimal.ZERO;

    private java.time.LocalDateTime approvedAt;
    private String buyerType;
    private String kioskId;
    private String failureReason;

    public String getPaymentId() {
        return String.format("pay_%04d", this.id);
    }

    public String getRedirectUrl() {
        return "https://pg.mock/checkout?paymentId=" + getPaymentId();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
        if (status == PaymentStatus.APPROVED) {
            this.approvedAt = java.time.LocalDateTime.now();
        }
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setPgTid(String pgTid) {
        this.pgTid = pgTid;
    }

    public void setPgProvider(String pgProvider) {
        this.pgProvider = pgProvider;
    }

    public void refund(BigDecimal amount) {
        this.refundedAmount = this.refundedAmount.add(amount);
        if (this.refundedAmount.compareTo(this.amount) >= 0) {
            this.status = PaymentStatus.REFUNDED;
        } else {
            this.status = PaymentStatus.PARTIALLY_REFUNDED;
        }
    }
}
