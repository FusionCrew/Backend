package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiMetricsDto {
    private SttMetrics stt;
    private LlmMetrics llm;
    private VisionMetrics vision;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SttMetrics {
        private int requests;
        private int failures;
        private double failureRate;
        private int avgLatencyMs;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LlmMetrics {
        private int requests;
        private int avgLatencyMs;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VisionMetrics {
        private int requests;
        private int avgLatencyMs;
    }
}
