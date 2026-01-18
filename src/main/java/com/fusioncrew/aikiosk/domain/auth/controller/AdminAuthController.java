package com.fusioncrew.aikiosk.domain.auth.controller;

import com.fusioncrew.aikiosk.domain.auth.dto.AdminLoginRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.RefreshTokenRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.TokenResponse;
import com.fusioncrew.aikiosk.domain.auth.service.AdminAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(adminAuthService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest req) {
        return ResponseEntity.ok(adminAuthService.refresh(req.getRefreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        String username = authentication.getName();
        adminAuthService.logout(username);
        return ResponseEntity.ok().build();
    }
}