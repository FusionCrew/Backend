package com.fusioncrew.aikiosk.domain.admin.dto;

import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentMethod;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminPaymentDetailResponse {
    private PaymentDetail payment;
    private OrderDetail order;
    private PgDetail pg;
    private BuyerDetail buyer;
    private AuditDetail audit;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class PaymentDetail {
        private String paymentId;
        private String orderId;
        private PaymentStatus status;
        private PaymentMethod method;
        private boolean isMock;
        private AmountDetail amount;
        private LocalDateTime requestedAt;
        private LocalDateTime approvedAt;
        private FailureDetail failure;
        private CancelDetail cancel;
        private RefundDetail refund;
    }

    @Getter
    @Builder
    public static class AmountDetail {
        private BigDecimal total;
        private String currency;
        private BigDecimal tax;
        private BigDecimal supply;
    }

    @Getter
    @Builder
    public static class FailureDetail {
        private String code;
        private String message;
        private LocalDateTime occurredAt;
    }

    @Getter
    @Builder
    public static class CancelDetail {
        private String reason;
        private LocalDateTime cancelledAt;
    }

    @Getter
    @Builder
    public static class RefundDetail {
        private BigDecimal amount;
        private LocalDateTime refundedAt;
    }

    @Getter
    @Builder
    public static class OrderDetail {
        private String orderId;
        private OrderStatus status;
        private int totalQuantity;
        private List<OrderItemDetail> items;
    }

    @Getter
    @Builder
    public static class OrderItemDetail {
        private String menuItemId;
        private String name;
        private int quantity;
        private int price;
    }

    @Getter
    @Builder
    public static class PgDetail {
        private String provider;
        private String tid;
        private String rawStatus;
        private String responseCode;
        private String responseMessage;
    }

    @Getter
    @Builder
    public static class BuyerDetail {
        private String type; // KIOSK, ADMIN, SYSTEM
        private String kioskId;
    }

    @Getter
    @Builder
    public static class AuditDetail {
        private String createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime lastUpdatedAt;
    }
}
