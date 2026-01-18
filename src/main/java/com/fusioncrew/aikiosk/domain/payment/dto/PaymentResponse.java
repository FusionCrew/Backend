package com.fusioncrew.aikiosk.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentMethod;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PaymentResponse {
    private String paymentId;
    private String orderId;
    private PaymentStatus status;
    private BigDecimal amount;
    private PaymentMethod method;
    private BigDecimal refundedAmount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String redirectUrl;

    private String timestamp;
    private String requestId;
}
