package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminKioskSessionDetailResponse {
    private String sessionId;
    private String language;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String timestamp;
    private String requestId;
}
