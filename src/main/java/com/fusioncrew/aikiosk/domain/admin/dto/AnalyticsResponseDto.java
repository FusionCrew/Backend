package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponseDto {
    private Long todayRevenue;
    private Double revenueChange;
    private Integer avgOrderValue;
    private Integer totalVisitors;
    private Double returnRate;
    private List<MenuRankingDto> topMenus;
    private List<String> aiInsights;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuRankingDto {
        private String name;
        private Long count;
    }
}
