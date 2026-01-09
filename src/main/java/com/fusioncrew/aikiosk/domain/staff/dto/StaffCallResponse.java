package com.fusioncrew.aikiosk.domain.staff.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffCallResponse {
    private boolean success;
    private CallData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CallData {
        private String callId;
        private String status;
    }
}
