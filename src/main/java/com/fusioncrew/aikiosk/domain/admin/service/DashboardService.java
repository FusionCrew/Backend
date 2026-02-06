package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.DashboardSummaryDto;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.stock.entity.Stock;
import com.fusioncrew.aikiosk.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    public DashboardSummaryDto getDashboardSummary() {
        List<Order> allOrders = orderRepository.findAll();

        // Calculate today's revenue (simplification: all orders since midnight)
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        long todayRevenue = allOrders.stream()
                .filter(o -> o.getCreatedAt().isAfter(todayStart))
                .filter(o -> o.getStatus() != OrderStatus.CANCELLED)
                .mapToLong(
                        o -> o.getItems().stream().mapToLong(item -> (long) item.getPrice() * item.getQuantity()).sum())
                .sum();

        // Count active orders (CONFIRMED, MAKING, READY)
        int activeOrders = (int) allOrders.stream()
                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED || o.getStatus() == OrderStatus.MAKING
                        || o.getStatus() == OrderStatus.READY)
                .count();

        // Low stock ingredients (below 10)
        List<DashboardSummaryDto.LowStockIngredientDto> lowStocks = stockRepository.findAll().stream()
                .filter(s -> s.getQuantity() < 10)
                .map(s -> DashboardSummaryDto.LowStockIngredientDto.builder()
                        .name(s.getIngredientName())
                        .stockLevel(s.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // Hourly sales (dummy for now)
        List<Integer> hourlySales = Arrays.asList(12, 18, 15, 25, 30, 45, 40, 35, 20, 15, 10, 5);

        // AI Activity Logs (dummy for now)
        List<DashboardSummaryDto.ActivityLogDto> aiLogs = new ArrayList<>();
        aiLogs.add(new DashboardSummaryDto.ActivityLogDto("14:20", "추천 메뉴 분석", "점심 시간 인기 메뉴 '불고기버거' 분석 완료"));
        aiLogs.add(new DashboardSummaryDto.ActivityLogDto("13:45", "재고 알림", "양상추 재고 부족 예상 (2시간 내)"));
        aiLogs.add(new DashboardSummaryDto.ActivityLogDto("12:10", "주문 패턴 탐지", "대규모 단체 주문(8건) 감지 및 조리 순서 최적화"));

        return DashboardSummaryDto.builder()
                .todayRevenue(todayRevenue)
                .revenueChange(12.5) // dummy
                .activeOrders(activeOrders)
                .lowStockIngredients(lowStocks)
                .hourlySales(hourlySales)
                .aiActivityLogs(aiLogs)
                .build();
    }
}
