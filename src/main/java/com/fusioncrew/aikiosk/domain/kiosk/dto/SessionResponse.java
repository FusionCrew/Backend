package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {

    private boolean success;
    private SessionData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionData {
        private String sessionId;
        private SessionStatus status;
        private String createdAt;
    }
}
