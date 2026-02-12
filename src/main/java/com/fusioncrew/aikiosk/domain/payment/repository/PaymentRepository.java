package com.fusioncrew.aikiosk.domain.payment.repository;

import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
        Optional<Payment> findTopByOrderIdAndStatusOrderByCreatedAtDesc(String orderId, PaymentStatus status);

        Optional<Payment> findByOrderId(String orderId);

        @org.springframework.data.jpa.repository.Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status IN :statuses AND (COALESCE(p.approvedAt, p.createdAt)) BETWEEN :start AND :end")
        java.math.BigDecimal sumSalesAmountByStatusesAndDateBetween(
                        @org.springframework.data.repository.query.Param("statuses") java.util.Collection<PaymentStatus> statuses,
                        @org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start,
                        @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);

        @org.springframework.data.jpa.repository.Query("SELECT SUM(p.refundedAmount) FROM Payment p WHERE p.status IN :statuses AND (COALESCE(p.updatedAt, p.createdAt)) BETWEEN :start AND :end")
        java.math.BigDecimal sumRefundedAmountByStatusesAndDateBetween(
                        @org.springframework.data.repository.query.Param("statuses") java.util.Collection<PaymentStatus> statuses,
                        @org.springframework.data.repository.query.Param("start") java.time.LocalDateTime start,
                        @org.springframework.data.repository.query.Param("end") java.time.LocalDateTime end);

        Long countByCreatedAtBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}
