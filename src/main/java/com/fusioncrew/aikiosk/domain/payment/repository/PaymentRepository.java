package com.fusioncrew.aikiosk.domain.payment.repository;

import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
