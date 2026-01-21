package com.fusioncrew.aikiosk.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PaymentDeclineRequest {
    private PgDetail pg;

    @Getter
    @NoArgsConstructor
    public static class PgDetail {
        private String provider;
        private String reason;
    }
}
