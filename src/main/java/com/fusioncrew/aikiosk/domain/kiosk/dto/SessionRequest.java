package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.InputMode;
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
public class SessionRequest {

    @NotBlank(message = "Language is required")
    private String language;

    @NotNull(message = "Accessibility settings are required")
    @Valid
    private AccessibilityDto accessibility;

    @NotNull(message = "Input mode is required")
    private InputMode inputMode;

    @NotNull(message = "Device info is required")
    @Valid
    private DeviceDto device;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessibilityDto {
        @NotNull(message = "largeText is required")
        private Boolean largeText;

        @NotNull(message = "highContrast is required")
        private Boolean highContrast;

        @NotNull(message = "voiceGuidance is required")
        private Boolean voiceGuidance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeviceDto {
        @NotBlank(message = "kioskId is required")
        private String kioskId;

        @NotBlank(message = "appVersion is required")
        private String appVersion;
    }
}
