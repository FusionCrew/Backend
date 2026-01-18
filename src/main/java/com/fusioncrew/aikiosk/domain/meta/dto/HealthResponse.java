package com.fusioncrew.aikiosk.domain.meta.dto;

import com.fusioncrew.aikiosk.domain.meta.entity.HealthStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthResponse {

    private boolean success;
    private HealthData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HealthData {
        private HealthStatus status;
        private String service;
        private String version;
    }
}
