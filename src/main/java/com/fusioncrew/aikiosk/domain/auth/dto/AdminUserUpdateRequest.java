package com.fusioncrew.aikiosk.domain.auth.dto;

import com.fusioncrew.aikiosk.domain.auth.entity.AdminRole;
import lombok.Getter;

@Getter
public class AdminUserUpdateRequest {
    private String password;
    private String name;
    private AdminRole role;
}