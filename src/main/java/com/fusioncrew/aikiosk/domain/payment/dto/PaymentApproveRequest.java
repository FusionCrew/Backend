package com.fusioncrew.aikiosk.domain.payment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentApproveRequest {
    private PgDetail pg;

    @Getter
    @NoArgsConstructor
    public static class PgDetail {
        private String provider;
        private String tid;
        private LocalDateTime approvedAt;
    }
}
