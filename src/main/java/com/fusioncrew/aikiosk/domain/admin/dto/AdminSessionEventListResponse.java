package com.fusioncrew.aikiosk.domain.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class AdminSessionEventListResponse {
    private List<EventItem> items;
    private String timestamp;
    private String requestId;

    @Getter
    @Builder
    public static class EventItem {
        private String eventId;
        private String sessionId;
        private String type;
        private LocalDateTime occurredAt;
        private Long durationSeconds;
        private String step;
        private Map<String, Object> metadata;
        private String kioskId;
    }
}
