package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.AiMetricsDto;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    public AiMetricsDto getAiMetrics() {
        // Mock data matching the screenshot
        return AiMetricsDto.builder()
                .stt(AiMetricsDto.SttMetrics.builder()
                        .requests(120)
                        .failures(6)
                        .failureRate(0.05)
                        .avgLatencyMs(480)
                        .build())
                .llm(AiMetricsDto.LlmMetrics.builder()
                        .requests(210)
                        .avgLatencyMs(920)
                        .build())
                .vision(AiMetricsDto.VisionMetrics.builder()
                        .requests(80)
                        .avgLatencyMs(210)
                        .build())
                .build();
    }
}
