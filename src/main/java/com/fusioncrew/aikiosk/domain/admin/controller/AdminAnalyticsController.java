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

    @GetMapping
    public ApiResponse<AnalyticsResponseDto> getAnalytics() {
        // Dummy data for now matching the frontend structure
        AnalyticsResponseDto response = AnalyticsResponseDto.builder()
                .todayRevenue(4250000L)
                .revenueChange(12.5)
                .avgOrderValue(14000)
                .totalVisitors(842)
                .returnRate(34.5)
                .topMenus(Arrays.asList(
                        new AnalyticsResponseDto.MenuRankingDto("더블 베이컨 치즈", 428),
                        new AnalyticsResponseDto.MenuRankingDto("스파이시 상하이", 386),
                        new AnalyticsResponseDto.MenuRankingDto("트러플 머쉬룸", 212),
                        new AnalyticsResponseDto.MenuRankingDto("갈릭 소스 프라이즈", 156),
                        new AnalyticsResponseDto.MenuRankingDto("밀크 쉐이크", 98)))
                .aiInsights(Arrays.asList(
                        "피크 타임 인력 보강 제안: 이번 주 금요일과 토요일 19시~21시 사이 주문량이 평소보다 25% 높을 것으로 예상됩니다.",
                        "메뉴 구성 최적화: '더블 베이컨 치즈버거'의 세트 판매 비중이 상승하고 있습니다. 런치 세트 강화를 권장합니다.",
                        "재방문 프로모션 제안: 최근 신규 고객은 증가했으나 재방문율이 소폭 하락했습니다. 쿠폰 발급 이벤트를 제안합니다.",
                        "재고 효율 분석: 신선 식품(패티, 채소류)의 폐기율이 지난주 대비 4% 감소했습니다."))
                .build();
        return ApiResponse.ok(response);
    }
}
