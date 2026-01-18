package com.fusioncrew.aikiosk.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    
    @NotBlank
    private String refreshToken;
}