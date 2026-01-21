package com.fusioncrew.aikiosk.domain.payment.dto;

import com.fusioncrew.aikiosk.domain.payment.entity.RefundReason;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentRefundRequest {
    private RefundReason reason;
    private BigDecimal amount;
}
