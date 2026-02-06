package com.fusioncrew.aikiosk.domain.auth.controller;

import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserCreateRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserResponse;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserUpdateRequest;
import com.fusioncrew.aikiosk.domain.auth.service.AdminUserService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.OffsetDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        var user = adminUserService.getEntity(authentication.getName());

        Map<String, Object> data = new HashMap<>();
        data.put("adminUserId", formatAdminUserId(user.getId()));
        data.put("username", user.getUsername());
        data.put("roles", List.of(toRoleString(user.getRole().name())));
        data.put("status", "ACTIVE"); // 엔티티에 상태필드 없어서 명세 맞춰 고정

        return ResponseEntity.ok(commonResponse(data));
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> list() {
        var users = adminUserService.listEntity();

        List<Map<String, Object>> items = users.stream().map(u -> {
            Map<String, Object> item = new HashMap<>();
            item.put("adminUserId", formatAdminUserId(u.getId()));
            item.put("username", u.getUsername());
            item.put("roles", List.of(toRoleString(u.getRole().name())));
            item.put("status", "ACTIVE");
            return item;
        }).toList();

        Map<String, Object> data = new HashMap<>();
        data.put("items", items);

        return ResponseEntity.ok(commonResponse(data));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<CreateAdminUserResponse>> create(@Valid @RequestBody AdminUserCreateRequest req) {

        AdminUserResponse created = adminUserService.create(req);

        String adminUserId = formatAdminUserId(created.getId());
        CreateAdminUserResponse data = CreateAdminUserResponse.builder()
                .adminUserId(adminUserId)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("관리자 계정 생성 성공", data));
    }

    @PatchMapping("/users/{adminUserId}")
    public ResponseEntity<ApiResponse<AdminUserResponse>> update(
            @PathVariable String adminUserId,
            @RequestBody AdminUserUpdateRequest req) {
        Long id = parseAdminUserId(adminUserId);
        AdminUserResponse data = adminUserService.update(id, req);
        return ResponseEntity.ok(ApiResponse.success("관리자 계정 수정 성공", data));
    }

    @DeleteMapping("/users/{adminUserId}")
    public ResponseEntity<ApiResponse<DeleteAdminUserResponse>> delete(@PathVariable String adminUserId) {
        Long id = parseAdminUserId(adminUserId);

        adminUserService.delete(id);

        DeleteAdminUserResponse data = DeleteAdminUserResponse.builder()
                .deleted(true)
                .adminUserId(formatAdminUserId(id))
                .build();

        return ResponseEntity.ok(ApiResponse.success("관리자 계정 삭제 성공", data));
    }

    @Getter
    @Builder
    public static class CreateAdminUserResponse {
        private String adminUserId;
    }

    @Getter
    @Builder
    public static class DeleteAdminUserResponse {
        private boolean deleted;
        private String adminUserId;
    }

    private String toRoleString(String roleName) {
        if (roleName == null)
            return "ROLE_ADMIN";
        if (roleName.startsWith("ROLE_"))
            return roleName;
        return "ROLE_" + roleName;
    }

    private Map<String, Object> commonResponse(Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", data);
        res.put("timestamp", OffsetDateTime.now().toString());
        res.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return res;
    }

    private String formatAdminUserId(Long id) {
        return String.format("adm_%04d", id);
    }

    private Long parseAdminUserId(String adminUserId) {
        String raw = adminUserId == null ? "" : adminUserId.trim();
        if (raw.startsWith("adm_"))
            raw = raw.substring("adm_".length());
        return Long.parseLong(raw);
    }
}