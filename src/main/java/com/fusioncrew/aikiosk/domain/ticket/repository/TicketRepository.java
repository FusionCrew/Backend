package com.fusioncrew.aikiosk.domain.ticket.repository;

import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByOrderId(Long orderId);
    long countByIssuedDate(LocalDate issuedDate);
}