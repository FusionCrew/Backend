package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminKioskSessionDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminKioskSessionListResponse;
import com.fusioncrew.aikiosk.domain.admin.service.AdminKioskSessionService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/kiosk-sessions")
@RequiredArgsConstructor
public class AdminKioskController {

    private final AdminKioskSessionService adminKioskSessionService;

    @GetMapping
    public ApiResponse<AdminKioskSessionListResponse> getKioskSessionList(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        // Default to today if null
        if (from == null)
            from = java.time.LocalDate.now().toString();
        if (to == null)
            to = java.time.LocalDate.now().toString();

        AdminKioskSessionListResponse response = adminKioskSessionService.getKioskSessionList(from, to);
        return ApiResponse.success("키오스크 세션 목록을 성공적으로 조회했습니다.", response);
    }

    @GetMapping("/{sessionId}")
    public ApiResponse<AdminKioskSessionDetailResponse> getKioskSessionDetail(
            @PathVariable String sessionId) {
        AdminKioskSessionDetailResponse response = adminKioskSessionService.getKioskSessionDetail(sessionId);
        return ApiResponse.success("키오스크 세션 상세 정보를 성공적으로 조회했습니다.", response);
    }
}
