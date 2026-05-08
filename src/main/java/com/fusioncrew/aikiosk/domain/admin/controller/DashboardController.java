package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.DashboardResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.DashboardSummaryDto;
import com.fusioncrew.aikiosk.domain.admin.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/dashboard/summary")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DashboardSummaryDto data = (date != null)
                ? dashboardService.getDashboardSummaryForDate(date)
                : dashboardService.getDashboardSummary();

        DashboardResponse response = DashboardResponse.builder()
                .success(true)
                .data(data)
                .timestamp(ZonedDateTime.now().format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.ok(response);
    }
}
