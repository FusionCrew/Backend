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

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody AdminLoginRequest req) {
        return ResponseEntity.ok(adminAuthService.login(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(Authentication authentication) {
        String username = authentication.getName();
        adminAuthService.logout(username);

        Map<String, Object> data = new HashMap<>();
        data.put("loggedOut", true);

        return ResponseEntity.ok(commonResponse(data));
    }

    private Map<String, Object> commonResponse(Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", data);
        res.put("timestamp", OffsetDateTime.now());
        res.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return res;
    }
}