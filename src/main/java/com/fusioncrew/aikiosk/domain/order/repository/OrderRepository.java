package com.fusioncrew.aikiosk.domain.order.repository;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderId(String orderId);

    Optional<Order> findBySessionId(String sessionId);

    Long countByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);

    @org.springframework.data.jpa.repository.Query("SELECT new com.fusioncrew.aikiosk.domain.admin.dto.AnalyticsResponseDto$MenuRankingDto(oi.name, SUM(oi.quantity)) "
            +
            "FROM Order o JOIN o.items oi " +
            "WHERE o.createdAt BETWEEN :start AND :end " +
            "AND o.status <> com.fusioncrew.aikiosk.domain.order.entity.OrderStatus.CANCELED " +
            "GROUP BY oi.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    java.util.List<com.fusioncrew.aikiosk.domain.admin.dto.AnalyticsResponseDto.MenuRankingDto> findTopSellingMenus(
            @org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start,
            @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end,
            org.springframework.data.domain.Pageable pageable);
}
