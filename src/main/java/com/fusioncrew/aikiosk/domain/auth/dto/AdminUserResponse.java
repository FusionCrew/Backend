package com.fusioncrew.aikiosk.domain.auth.dto;

import com.fusioncrew.aikiosk.domain.auth.entity.AdminRole;
import com.fusioncrew.aikiosk.domain.auth.entity.AdminUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminUserResponse {
    private Long id;
    private String username;
    private String name;
    private AdminRole role;

    public static AdminUserResponse from(AdminUser u) {
        return AdminUserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .name(u.getName())
                .role(u.getRole())
                .build();
    }
}