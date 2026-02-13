package com.fusioncrew.aikiosk.domain.ticket.dto;

import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;

public class TicketDtos {

        public record IssueTicketRequest(
                        String orderId,
                        String paymentId) {
        }

        public record TicketResponse(
                        String ticketId,
                        Integer number,
                        String ticketNumber,
                        TicketStatus status) {
        }

        public record TicketDetailResponse(
                        String ticketId,
                        Integer number,
                        String ticketNumber,
                        TicketStatus status,
                        Integer estimatedWaitMin) {
        }
}