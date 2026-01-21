package com.fusioncrew.aikiosk.domain.ticket.repository;

import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByOrderId(String orderId);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByPaymentId(String paymentId);

    long countByStatus(TicketStatus status);
}
