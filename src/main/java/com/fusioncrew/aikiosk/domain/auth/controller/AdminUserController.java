package com.fusioncrew.aikiosk.domain.auth.controller;

import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserCreateRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserResponse;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserUpdateRequest;
import com.fusioncrew.aikiosk.domain.auth.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping("/me")
    public ResponseEntity<AdminUserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(adminUserService.me(authentication.getName()));
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> list() {
        return ResponseEntity.ok(adminUserService.list());
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUserResponse> create(@Valid @RequestBody AdminUserCreateRequest req) {
        return ResponseEntity.ok(adminUserService.create(req));
    }

    @PatchMapping("/users/{adminUserId}")
    public ResponseEntity<AdminUserResponse> update(
            @PathVariable Long adminUserId,
            @RequestBody AdminUserUpdateRequest req
    ) {
        return ResponseEntity.ok(adminUserService.update(adminUserId, req));
    }

    @DeleteMapping("/users/{adminUserId}")
    public ResponseEntity<Void> delete(@PathVariable Long adminUserId) {
        adminUserService.delete(adminUserId);
        return ResponseEntity.ok().build();
    }
}