package com.fusioncrew.aikiosk.domain.auth.service;

import com.fusioncrew.aikiosk.domain.auth.dto.AdminLoginRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.TokenResponse;
import com.fusioncrew.aikiosk.domain.auth.entity.AdminUser;
import com.fusioncrew.aikiosk.domain.auth.repository.AdminUserRepository;
import com.fusioncrew.aikiosk.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    /* login (username, password 검증 -> accessToken 발급 -> 
    refreshToken 발급(동일 createToken 사용, AdminUser 엔티티에 저장해서 refresh, logout 검증에 사용) */
    @Transactional
    public TokenResponse login(AdminLoginRequest req) {
        AdminUser user = adminUserRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String role = user.getRole().name();

        String accessToken = jwtTokenProvider.createToken(user.getUsername(), role);
        String refreshToken = jwtTokenProvider.createToken(user.getUsername(), "REFRESH");

        user.updateRefreshToken(refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // refresh (refreshToken 유효성 검사 -> username 추출 -> DB의 refreshToken 일치 확인 -> 새 access/refresh 발급)
    @Transactional
    public TokenResponse refresh(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsername(refreshToken);

        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (user.getRefreshToken() == null || !user.getRefreshToken().equals(refreshToken)) {
            throw new BadCredentialsException("Refresh token mismatch");
        }

        String role = user.getRole().name();

        String newAccessToken = jwtTokenProvider.createToken(user.getUsername(), role);
        String newRefreshToken = jwtTokenProvider.createToken(user.getUsername(), "REFRESH");

        user.updateRefreshToken(newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    // logout (저장된 refreshToken 제거, refresh 불가)
    @Transactional
    public void logout(String username) {
        if (username == null || username.isBlank()) return;

        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Admin not found"));

        user.clearRefreshToken();
    }
}