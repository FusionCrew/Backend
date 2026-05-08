package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.DashboardSummaryDto;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        private final IngredientRepository ingredientRepository;

        public DashboardSummaryDto getDashboardSummary() {
                return getDashboardSummaryForDate(LocalDate.now());
        }

        public DashboardSummaryDto getDashboardSummaryForDate(LocalDate date) {
                List<Order> allOrders = orderRepository.findAll();

                LocalDateTime dayStart = date.atStartOfDay();
                LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

                // Filter orders for the given date
                List<Order> dayOrders = allOrders.stream()
                                .filter(o -> !o.getCreatedAt().isBefore(dayStart) && !o.getCreatedAt().isAfter(dayEnd))
                                .filter(o -> o.getStatus() != OrderStatus.CANCELED)
                                .collect(Collectors.toList());

                // Calculate day's revenue
                long dayRevenue = dayOrders.stream()
                                .mapToLong(o -> o.getItems().stream()
                                                .mapToLong(item -> (long) item.getUnitPrice() * item.getQuantity())
                                                .sum())
                                .sum();

                // Calculate previous day's revenue for comparison
                LocalDateTime prevDayStart = date.minusDays(1).atStartOfDay();
                LocalDateTime prevDayEnd = date.minusDays(1).atTime(LocalTime.MAX);
                long prevDayRevenue = allOrders.stream()
                                .filter(o -> !o.getCreatedAt().isBefore(prevDayStart)
                                                && !o.getCreatedAt().isAfter(prevDayEnd))
                                .filter(o -> o.getStatus() != OrderStatus.CANCELED)
                                .mapToLong(o -> o.getItems().stream()
                                                .mapToLong(item -> (long) item.getUnitPrice() * item.getQuantity())
                                                .sum())
                                .sum();

                double revenueChange = prevDayRevenue > 0
                                ? Math.round((dayRevenue - prevDayRevenue) * 1000.0 / prevDayRevenue) / 10.0
                                : 0.0;

                // Count active orders
                int activeOrders = (int) allOrders.stream()
                                .filter(o -> o.getStatus() == OrderStatus.CONFIRMED
                                                || o.getStatus() == OrderStatus.MAKING
                                                || o.getStatus() == OrderStatus.READY)
                                .count();

                // Low stock ingredients (below 10)
                List<DashboardSummaryDto.LowStockIngredientDto> lowStocks = stockRepository.findAll().stream()
                                .filter(s -> s.getQuantity() < 10)
                                .map(s -> {
                                        String ingredientName = ingredientRepository
                                                        .findByIngredientId(s.getIngredientId())
                                                        .map(ing -> ing.getName())
                                                        .orElse(s.getIngredientId());
                                        return DashboardSummaryDto.LowStockIngredientDto.builder()
                                                        .name(ingredientName)
                                                        .stockLevel(s.getQuantity())
                                                        .build();
                                })
                                .collect(Collectors.toList());

                // Hourly sales from REAL order data (0~23시)
                List<Long> hourlySales = calculateHourlySales(dayOrders);

                // AI Activity Logs (dummy for now)
                List<DashboardSummaryDto.ActivityLogDto> aiLogs = new ArrayList<>();
                aiLogs.add(new DashboardSummaryDto.ActivityLogDto("14:20", "추천 메뉴 분석",
                                "점심 시간 인기 메뉴 '불고기버거' 분석 완료"));
                aiLogs.add(new DashboardSummaryDto.ActivityLogDto("13:45", "재고 알림",
                                "양상추 재고 부족 예상 (2시간 내)"));
                aiLogs.add(new DashboardSummaryDto.ActivityLogDto("12:10", "주문 패턴 탐지",
                                "대규모 단체 주문(8건) 감지 및 조리 순서 최적화"));

                return DashboardSummaryDto.builder()
                                .todayRevenue(dayRevenue)
                                .revenueChange(revenueChange)
                                .activeOrders(activeOrders)
                                .lowStockIngredients(lowStocks)
                                .hourlySales(hourlySales)
                                .aiActivityLogs(aiLogs)
                                .build();
        }

        /**
         * Calculate hourly sales revenue from order data.
         * Returns a list of 24 values (index 0 = 00시, index 23 = 23시),
         * each representing total revenue in that hour.
         */
        private List<Long> calculateHourlySales(List<Order> orders) {
                long[] hourlyRevenue = new long[24];
                for (Order order : orders) {
                        int hour = order.getCreatedAt().getHour();
                        long orderTotal = order.getItems().stream()
                                        .mapToLong(item -> (long) item.getUnitPrice() * item.getQuantity())
                                        .sum();
                        hourlyRevenue[hour] += orderTotal;
                }
                List<Long> result = new ArrayList<>();
                for (long v : hourlyRevenue) {
                        result.add(v);
                }
                return result;
        }
}
