package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.AnalyticsResponseDto;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminAnalyticsService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public AnalyticsResponseDto getAnalytics(String startDateStr, String endDateStr) {
        LocalDateTime startDateTime = LocalDate.parse(startDateStr).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(endDateStr).atTime(LocalTime.MAX);

        // 1. Total Revenue (Period)
        BigDecimal periodRevenue = paymentRepository.sumAmountByStatusAndCreatedAtBetween(
                PaymentStatus.APPROVED, startDateTime, endDateTime);
        if (periodRevenue == null)
            periodRevenue = BigDecimal.ZERO;

        // 2. Total Visitors (Orders count in period)
        Long totalVisitors = orderRepository.countByCreatedAtBetween(startDateTime, endDateTime);

        // 3. Avg Order Value
        long avgOrderValue = totalVisitors > 0 ? periodRevenue.longValue() / totalVisitors : 0;

        // 4. Return Rate (Mock for now, hard to calculate without user tracking)
        double returnRate = 0.0;

        // 5. Top Menus
        List<AnalyticsResponseDto.MenuRankingDto> topMenus = orderRepository.findTopSellingMenus(
                startDateTime, endDateTime, PageRequest.of(0, 5));

        // 6. AI Insights (Mock based on data)
        List<String> insights = generateInsights(periodRevenue, totalVisitors, topMenus);

        return AnalyticsResponseDto.builder()
                .todayRevenue(periodRevenue.longValue()) // Reusing field for period revenue
                .revenueChange(0.0) // Requires comparison with previous period
                .avgOrderValue((int) avgOrderValue)
                .totalVisitors(totalVisitors.intValue())
                .returnRate(returnRate)
                .topMenus(topMenus)
                .aiInsights(insights)
                .build();
    }

    private List<String> generateInsights(BigDecimal revenue, Long visitors,
            List<AnalyticsResponseDto.MenuRankingDto> topMenus) {
        String topMenuName = topMenus.isEmpty() ? "없음" : topMenus.get(0).getName();
        return Arrays.asList(
                String.format("매출 분석: 선택된 기간 동안 총 매출은 %,d원, 방문자는 %d명입니다.", revenue.longValue(), visitors),
                String.format("인기 메뉴: '%s' 메뉴가 가장 많이 판매되었습니다. 재고 확보를 권장합니다.", topMenuName),
                "트렌드: 주말 저녁 시간대 매출 비중이 높습니다. 인력 배치를 조정하세요.",
                "프로모션: 객단가를 높이기 위해 세트 메뉴 할인을 고려해보세요.");
    }
}
