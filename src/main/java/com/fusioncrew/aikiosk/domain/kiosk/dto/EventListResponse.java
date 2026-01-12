package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventListResponse {

    private boolean success;
    private EventListData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventListData {
        private String sessionId;
        private List<EventItem> items;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventItem {
        private String eventId;
        private EventType type;
        private String occurredAt;
    }
}
