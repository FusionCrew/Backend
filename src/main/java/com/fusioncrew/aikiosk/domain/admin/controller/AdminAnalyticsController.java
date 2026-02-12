package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AnalyticsResponseDto;
import com.fusioncrew.aikiosk.domain.admin.service.DashboardService; // Reusing service logic or keeping it separate
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@RequiredArgsConstructor
public class AdminAnalyticsController {

        private final com.fusioncrew.aikiosk.domain.admin.service.AdminAnalyticsService adminAnalyticsService;

        @GetMapping
        public ApiResponse<AnalyticsResponseDto> getAnalytics(
                        @org.springframework.web.bind.annotation.RequestParam(required = false) String startDate,
                        @org.springframework.web.bind.annotation.RequestParam(required = false) String endDate) {

                // Default to today if not provided
                if (startDate == null)
                        startDate = java.time.LocalDate.now().toString();
                if (endDate == null)
                        endDate = java.time.LocalDate.now().toString();

                AnalyticsResponseDto response = adminAnalyticsService.getAnalytics(startDate, endDate);
                return ApiResponse.ok(response);
        }
}
