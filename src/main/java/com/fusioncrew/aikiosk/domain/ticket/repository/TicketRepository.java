package com.fusioncrew.aikiosk.domain.ticket.repository;

import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    boolean existsByPaymentId(String paymentId);

    long countByStatus(TicketStatus status);
}
