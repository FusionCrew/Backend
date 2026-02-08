package com.fusioncrew.aikiosk.domain.payment.repository;

import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findTopByOrderIdAndStatusOrderByCreatedAtDesc(String orderId, PaymentStatus status);
}
