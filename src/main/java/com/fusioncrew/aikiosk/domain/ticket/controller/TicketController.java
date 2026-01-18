package com.fusioncrew.aikiosk.domain.ticket.controller;

import com.fusioncrew.aikiosk.domain.ticket.dto.TicketCreateRequest;
import com.fusioncrew.aikiosk.domain.ticket.dto.TicketResponse;
import com.fusioncrew.aikiosk.domain.ticket.service.TicketService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kiosk/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TicketResponse> createTicket(@RequestBody TicketCreateRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return ApiResponse.success("대기표가 성공적으로 발급되었습니다.", response);
    }
}
