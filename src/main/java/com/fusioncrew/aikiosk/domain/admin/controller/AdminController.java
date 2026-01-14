package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AiMetricsDto;
import com.fusioncrew.aikiosk.domain.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/ai/metrics")
    public ResponseEntity<AdminResponse<AiMetricsDto>> getAiMetrics() {
        AiMetricsDto metrics = adminService.getAiMetrics();

        AdminResponse<AiMetricsDto> response = AdminResponse.<AiMetricsDto>builder()
                .success(true)
                .data(metrics)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.ok(response);
    }
}
