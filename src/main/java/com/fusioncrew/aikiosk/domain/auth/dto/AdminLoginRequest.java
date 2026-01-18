package com.fusioncrew.aikiosk.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminLoginRequest {
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
}