package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AdminKioskSessionListResponse {
    private List<SessionItem> items;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class SessionItem {
        private String sessionId;
        private String kioskId;
        private String status;
        private LocalDateTime startedAt;
        private LocalDateTime endedAt;
        private long durationSec;
        private long orderCount;
        private BigDecimal totalAmount;
        private long paymentCount;
        private long canceledCount;
    }
}
