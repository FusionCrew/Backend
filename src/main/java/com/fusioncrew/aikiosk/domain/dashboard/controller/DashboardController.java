package com.fusioncrew.aikiosk.domain.dashboard.controller;

import com.fusioncrew.aikiosk.domain.dashboard.dto.DashboardResponse;
import com.fusioncrew.aikiosk.domain.dashboard.dto.DashboardSummaryDto;
import com.fusioncrew.aikiosk.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardResponse> getDashboardSummary() {
        DashboardSummaryDto data = dashboardService.getDashboardSummary();

        DashboardResponse response = DashboardResponse.builder()
                .success(true)
                .data(data)
                .timestamp(ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.ok(response);
    }
}
