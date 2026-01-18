package com.fusioncrew.aikiosk.domain.auth.dto;

import com.fusioncrew.aikiosk.domain.auth.entity.AdminRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminUserCreateRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotNull
    private AdminRole role;
}