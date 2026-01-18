package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.*;
import com.fusioncrew.aikiosk.domain.admin.service.AdminTicketService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
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
        return ApiResponse.success("티켓 목록을 성공적으로 조회했습니다.", response);
    }

    @PostMapping("/{ticketId}/call")
    public ApiResponse<AdminTicketCallResponse> callTicket(
            @PathVariable String ticketId,
            @RequestBody AdminTicketCallRequest request) {
        AdminTicketCallResponse response = adminTicketService.callTicket(ticketId, request);
        return ApiResponse.success("티켓 호출이 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/{ticketId}/serve")
    public ApiResponse<AdminTicketServeResponse> serveTicket(
            @PathVariable String ticketId,
            @RequestBody AdminTicketServeRequest request) {
        AdminTicketServeResponse response = adminTicketService.serveTicket(ticketId, request);
        return ApiResponse.success("티켓 완료 처리가 성공적으로 완료되었습니다.", response);
    }
}
