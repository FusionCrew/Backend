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
public class AdminPaymentListResponse {
    private List<PaymentInfo> items;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class PaymentInfo {
        private String paymentId;
        private String orderId;
        private BigDecimal amount;
        private String currency;
        private PaymentStatus status;
        private PaymentMethod method;
        private boolean isMock;
        private LocalDateTime requestedAt;
        private LocalDateTime approvedAt;
        private OrderStatus orderStatus;
        private BuyerInfo buyer;
        private String failureReason;
    }

    @Getter
    @Builder
    public static class BuyerInfo {
        private String type; // KIOSK, ADMIN, SYSTEM
        private String kioskId;
    }
}
