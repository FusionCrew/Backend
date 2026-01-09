package com.fusioncrew.aikiosk.domain.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDto {
    private SalesDto sales;
    private OrdersDto orders;
    private SessionsDto sessions;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesDto {
        private Long today;
        private String currency;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrdersDto {
        private Integer count;
        private Double failedRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionsDto {
        private Integer count;
        private Integer avgDurationSec;
    }
}
