package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.*;
import com.fusioncrew.aikiosk.domain.admin.service.AdminTicketService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/tickets")
@RequiredArgsConstructor
public class AdminTicketController {

    private final AdminTicketService adminTicketService;

    @GetMapping
    public ApiResponse<AdminTicketListResponse> getTicketList() {
        AdminTicketListResponse response = adminTicketService.getTicketList();
        return ApiResponse.ok(response);
    }

    @PostMapping("/{ticketId}/call")
    public ApiResponse<AdminTicketCallResponse> callTicket(
            @PathVariable String ticketId,
            @RequestBody AdminTicketCallRequest request) {
        AdminTicketCallResponse response = adminTicketService.callTicket(ticketId, request);
        return ApiResponse.ok(response);
    }

    @PostMapping("/{ticketId}/serve")
    public ApiResponse<AdminTicketServeResponse> serveTicket(
            @PathVariable String ticketId,
            @RequestBody AdminTicketServeRequest request) {
        AdminTicketServeResponse response = adminTicketService.serveTicket(ticketId, request);
        return ApiResponse.ok(response);
    }
}
