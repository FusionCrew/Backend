package com.fusioncrew.aikiosk.domain.admin.dto;

import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminTicketCallResponse {
    private String ticketId;
    private TicketStatus status;
}
