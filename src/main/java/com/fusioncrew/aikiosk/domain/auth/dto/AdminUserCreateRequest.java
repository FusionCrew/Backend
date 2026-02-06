package com.fusioncrew.aikiosk.domain.auth.dto;

import com.fusioncrew.aikiosk.domain.auth.entity.AdminRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminUserCreateRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotEmpty
    private List<AdminRole> roles;

    @NotBlank
    private String status;

    @NotBlank
    private String name;
}