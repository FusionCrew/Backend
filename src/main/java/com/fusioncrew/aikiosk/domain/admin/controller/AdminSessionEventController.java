package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminSessionEventDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminSessionEventListResponse;
import com.fusioncrew.aikiosk.domain.admin.service.AdminKioskSessionService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/session-events")
@RequiredArgsConstructor
public class AdminSessionEventController {

    private final AdminKioskSessionService adminKioskSessionService;

    @GetMapping
    public ApiResponse<AdminSessionEventListResponse> searchSessionEvents(
            @RequestParam(required = false) String type,
            @RequestParam String from,
            @RequestParam(required = false) String to,
            @RequestParam(required = false) String sessionId) {
        AdminSessionEventListResponse response = adminKioskSessionService.searchSessionEvents(type, from, to,
                sessionId);
        return ApiResponse.success("세션 이벤트를 성공적으로 조회했습니다.", response);
    }

    @GetMapping("/{eventId}")
    public ApiResponse<AdminSessionEventDetailResponse> getSessionEventDetail(
            @PathVariable String eventId) {
        AdminSessionEventDetailResponse response = adminKioskSessionService.getSessionEventDetail(eventId);
        return ApiResponse.success("세션 이벤트 상세 정보를 성공적으로 조회했습니다.", response);
    }
}
