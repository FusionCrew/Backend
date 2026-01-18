package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.RecommendationBasis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskRecommendationResponse {

    private boolean success;
    private RecommendationData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationData {
        private RecommendationBasis basis;
        private List<RecommendationItemDto> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationItemDto {
        private String menuItemId;
        private String name;
        private String reason;
    }
}
