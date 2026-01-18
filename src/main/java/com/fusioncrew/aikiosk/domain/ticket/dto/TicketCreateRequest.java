package com.fusioncrew.aikiosk.domain.ticket.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TicketCreateRequest {
    private String orderId;
    private String paymentId;
}
