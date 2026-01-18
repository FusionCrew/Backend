package com.fusioncrew.aikiosk.domain.payment.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    CARD("카드", true, true, null),
    CASH("현금", true, true, null),
    VOUCHER("바우처", true, false, new BigDecimal("100000"));

    private final String displayName;
    private final boolean enabled;
    private final boolean supportsRefund;
    private final BigDecimal maxAmount;
}
