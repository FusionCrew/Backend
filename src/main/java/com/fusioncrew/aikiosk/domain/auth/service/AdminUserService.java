package com.fusioncrew.aikiosk.domain.auth.service;

import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserCreateRequest;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserResponse;
import com.fusioncrew.aikiosk.domain.auth.dto.AdminUserUpdateRequest;
import com.fusioncrew.aikiosk.domain.auth.entity.AdminUser;
import com.fusioncrew.aikiosk.domain.auth.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public AdminUserResponse me(String username) {
        AdminUser user = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return AdminUserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<AdminUserResponse> list() {
        return adminUserRepository.findAll()
                .stream()
                .map(AdminUserResponse::from)
                .toList();
    }

    @Transactional
    public AdminUserResponse create(AdminUserCreateRequest req) {
        if (adminUserRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("username already exists");
        }

        AdminUser user = AdminUser.builder()
                .username(req.getUsername())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .role(req.getRole())
                .build();

        AdminUser saved = adminUserRepository.save(user);
        return AdminUserResponse.from(saved);
    }

    @Transactional
    public AdminUserResponse update(Long adminUserId, AdminUserUpdateRequest req) {
        AdminUser user = adminUserRepository.findById(adminUserId)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        if (req.getName() != null && !req.getName().isBlank()) {
            user.changeName(req.getName());
        }
        if (req.getRole() != null) {
            user.changeRole(req.getRole());
        }
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.changePasswordHash(passwordEncoder.encode(req.getPassword()));
        }

        return AdminUserResponse.from(user);
    }

    @Transactional
    public void delete(Long adminUserId) {
        if (!adminUserRepository.existsById(adminUserId)) {
            throw new UsernameNotFoundException("Admin not found");
        }
        adminUserRepository.deleteById(adminUserId);
    }
}