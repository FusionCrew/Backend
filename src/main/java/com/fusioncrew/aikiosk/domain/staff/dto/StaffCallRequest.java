package com.fusioncrew.aikiosk.domain.staff.dto;

import com.fusioncrew.aikiosk.domain.staff.entity.CallReason;
import com.fusioncrew.aikiosk.domain.staff.entity.KioskScreen;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffCallRequest {

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotNull(message = "Call reason is required")
    private CallReason reason;

    private String message;

    @Valid
    private ContextDto context;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContextDto {
        private KioskScreen screen;
        private String orderId;
    }
}
