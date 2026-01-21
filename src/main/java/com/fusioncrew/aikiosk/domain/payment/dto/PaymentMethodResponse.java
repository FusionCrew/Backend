package com.fusioncrew.aikiosk.domain.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

import java.util.List;

@Getter
@Builder
public class PaymentMethodResponse {
    private List<MethodInfo> methods;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class MethodInfo {
        private String code;
        private String displayName;
        private boolean enabled;
        private boolean supportsRefund;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BigDecimal maxAmount;
    }
}
