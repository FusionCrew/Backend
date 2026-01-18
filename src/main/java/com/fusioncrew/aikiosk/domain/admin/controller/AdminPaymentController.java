package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminPaymentDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminPaymentListResponse;
import com.fusioncrew.aikiosk.domain.admin.service.AdminPaymentService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/payments")
@RequiredArgsConstructor
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @GetMapping
    public ApiResponse<AdminPaymentListResponse> getPaymentList(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        AdminPaymentListResponse response = adminPaymentService.getPaymentList(status, method, orderId, from, to, page,
                size);
        return ApiResponse.success("결제 목록 조회가 성공적으로 완료되었습니다.", response);
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<AdminPaymentDetailResponse> getPaymentDetail(@PathVariable String paymentId) {
        AdminPaymentDetailResponse response = adminPaymentService.getPaymentDetail(paymentId);
        return ApiResponse.success("결제 상세 조회가 성공적으로 완료되었습니다.", response);
    }
}
