package com.fusioncrew.aikiosk.domain.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PaymentReceiptResponse {
    private String paymentId;
    private String receiptNo;
    private LocalDateTime issuedAt;
    private List<ReceiptItem> items;
    private BigDecimal total;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class ReceiptItem {
        private String name;
        private int qty;
        private int price;
    }
}
