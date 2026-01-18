package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.InputMode;
import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionDetailResponse {

    private boolean success;
    private SessionDetailData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionDetailData {
        private String sessionId;
        private SessionStatus status;
        private String language;
        private InputMode inputMode;
        private String startedAt;
        private String endedAt;
    }
}
