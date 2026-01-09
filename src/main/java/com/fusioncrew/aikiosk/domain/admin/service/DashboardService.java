package com.fusioncrew.aikiosk.domain.dashboard.service;

import com.fusioncrew.aikiosk.domain.dashboard.dto.DashboardSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    /**
     * 대시보드 요약 데이터를 집계하여 반환합니다.
     * 향후 OrderRepository, SessionRepository 등에서 실제 데이터를 조회하도록 확장됩니다.
     */
    public DashboardSummaryDto getDashboardSummary() {
        // TODO: 실제 레포지토리 연동 로직 (현재는 구조 확보를 위한 샘플 데이터 반환)

        return DashboardSummaryDto.builder()
                .sales(DashboardSummaryDto.SalesDto.builder()
                        .today(fetchCurrentSales())
                        .currency("KRW")
                        .build())
                .orders(DashboardSummaryDto.OrdersDto.builder()
                        .count(fetchOrderCount())
                        .failedRate(calculateFailedRate())
                        .build())
                .sessions(DashboardSummaryDto.SessionsDto.builder()
                        .count(fetchSessionCount())
                        .avgDurationSec(calculateAvgDuration())
                        .build())
                .build();
    }

    // 아래 메서드들은 추후 DB 조회 로직으로 대체됩니다.
    private Long fetchCurrentSales() {
        return 352000L;
    }

    private Integer fetchOrderCount() {
        return 42;
    }

    private Double calculateFailedRate() {
        return 0.03;
    }

    private Integer fetchSessionCount() {
        return 55;
    }

    private Integer calculateAvgDuration() {
        return 420;
    }
}
