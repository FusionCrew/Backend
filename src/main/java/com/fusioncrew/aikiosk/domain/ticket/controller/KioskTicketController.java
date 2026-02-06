package com.fusioncrew.aikiosk.domain.ticket.controller;

import com.fusioncrew.aikiosk.domain.ticket.dto.TicketDtos;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kiosk/tickets")
@RequiredArgsConstructor
public class KioskTicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> issue(
            @RequestBody TicketDtos.IssueTicketRequest req
    ) {
        TicketDtos.TicketResponse data = ticketService.issue(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse(data));
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable String ticketId) {
        TicketDtos.TicketDetailResponse data = ticketService.getDetail(ticketId);

        return ResponseEntity.ok(commonResponse(data));
    }

    private Map<String, Object> commonResponse(Object data) {
        Map<String, Object> res = new HashMap<>();
        res.put("success", true);
        res.put("data", data);
        res.put("timestamp", OffsetDateTime.now());
        res.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return res;
    }
}