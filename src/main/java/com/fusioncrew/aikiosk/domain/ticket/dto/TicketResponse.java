package com.fusioncrew.aikiosk.domain.ticket.dto;

import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TicketResponse {
    private String ticketId;
    private Integer number;
    private TicketStatus status;
}
