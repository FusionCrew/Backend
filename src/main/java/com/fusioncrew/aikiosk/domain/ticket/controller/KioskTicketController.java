package com.fusioncrew.aikiosk.domain.ticket.controller;

import com.fusioncrew.aikiosk.domain.ticket.dto.TicketDtos;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/kiosk/tickets")
@RequiredArgsConstructor
public class KioskTicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<com.fusioncrew.aikiosk.global.api.ApiResponse<TicketDtos.TicketResponse>> issue(
            @RequestBody TicketDtos.IssueTicketRequest req) {
        TicketDtos.TicketResponse data = ticketService.issue(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(com.fusioncrew.aikiosk.global.api.ApiResponse.ok(data));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<com.fusioncrew.aikiosk.global.api.ApiResponse<TicketDtos.TicketDetailResponse>> get(
            @PathVariable String ticketId) {
        TicketDtos.TicketDetailResponse data = ticketService.getDetail(ticketId);

        return ResponseEntity.ok(com.fusioncrew.aikiosk.global.api.ApiResponse.ok(data));
    }
}