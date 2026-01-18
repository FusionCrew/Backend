package com.fusioncrew.aikiosk.domain.ticket.controller;

import com.fusioncrew.aikiosk.domain.ticket.dto.TicketDtos;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kiosk")
public class KioskTicketController {

    private final TicketService ticketService;

    public KioskTicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/tickets")
    public ResponseEntity<TicketDtos.TicketResponse> issue(@RequestBody TicketDtos.IssueTicketRequest req) {
        return ResponseEntity.ok(toRes(ticketService.issue(req.orderId())));
    }

    @GetMapping("/tickets/{ticketId}")
    public ResponseEntity<TicketDtos.TicketResponse> get(@PathVariable Long ticketId) {
        return ResponseEntity.ok(toRes(ticketService.get(ticketId)));
    }

    private TicketDtos.TicketResponse toRes(Ticket t) {
        return new TicketDtos.TicketResponse(
                t.getId(),
                t.getOrderId(),
                t.getIssuedDate(),
                t.getDailyNumber(),
                t.getStatus(),
                t.getCreatedAt()
        );
    }
}