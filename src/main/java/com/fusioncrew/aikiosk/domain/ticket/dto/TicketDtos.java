package com.fusioncrew.aikiosk.domain.ticket.dto;

import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TicketDtos {

    public record IssueTicketRequest(Long orderId) {
    }

    public record TicketResponse(
            Long ticketId,
            String orderId,
            LocalDate issuedDate,
            Integer dailyNumber,
            TicketStatus status,
            LocalDateTime createdAt) {
    }
}