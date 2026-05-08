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
public class DashboardSummaryDto {
    private Long todayRevenue;
    private Double revenueChange;
    private Integer activeOrders;
    private List<LowStockIngredientDto> lowStockIngredients;
    private List<Long> hourlySales;
    private List<ActivityLogDto> aiActivityLogs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowStockIngredientDto {
        private String name;
        private Integer stockLevel;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityLogDto {
        private String time;
        private String title;
        private String desc;
    }
}
