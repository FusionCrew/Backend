package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class AdminSessionEventDetailResponse {
    private String eventId;
    private String sessionId;
    private String type;
    private Map<String, Object> payload;
    private LocalDateTime occurredAt;
    private String timestamp;
    private String requestId;
}
