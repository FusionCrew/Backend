package com.fusioncrew.aikiosk.domain.payment.dto;

import com.fusioncrew.aikiosk.domain.payment.entity.PaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentRequest {
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentMethod method;
    private boolean mock;
    private String returnUrl;
}
