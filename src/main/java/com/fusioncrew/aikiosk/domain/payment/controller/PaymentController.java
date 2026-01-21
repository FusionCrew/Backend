package com.fusioncrew.aikiosk.domain.payment.controller;

import com.fusioncrew.aikiosk.domain.payment.dto.*;
import com.fusioncrew.aikiosk.domain.payment.service.PaymentService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);
        return ApiResponse.success("결제 생성이 성공적으로 완료되었습니다.", response);
    }

    @GetMapping("/methods")
    public ApiResponse<PaymentMethodResponse> getPaymentMethods() {
        PaymentMethodResponse response = paymentService.getPaymentMethods();
        return ApiResponse.success("결제 수단 목록 조회가 성공적으로 완료되었습니다.", response);
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponse> getPayment(@PathVariable String paymentId) {
        PaymentResponse response = paymentService.getPayment(paymentId);
        return ApiResponse.success("결제 정보 조회가 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/{paymentId}/approve")
    public ApiResponse<PaymentResponse> approvePayment(
            @PathVariable String paymentId,
            @RequestBody PaymentApproveRequest request) {
        PaymentResponse response = paymentService.approvePayment(paymentId, request);
        return ApiResponse.success("결제 승인이 성공적으로 완료되었습니다.", response);
    }

    @PostMapping("/{paymentId}/decline")
    public ApiResponse<PaymentResponse> declinePayment(
            @PathVariable String paymentId,
            @RequestBody PaymentDeclineRequest request) {
        PaymentResponse response = paymentService.declinePayment(paymentId, request);
        return ApiResponse.success("결제 거절이 성공적으로 완료되었습니다.", response);
    }

    @PatchMapping("/{paymentId}/refund")
    public ApiResponse<PaymentResponse> refundPayment(
            @PathVariable String paymentId,
            @RequestBody PaymentRefundRequest request) {
        PaymentResponse response = paymentService.refundPayment(paymentId, request);
        return ApiResponse.success("결제 환불 처리가 성공적으로 완료되었습니다.", response);
    }

    @GetMapping("/{paymentId}/receipt")
    public ApiResponse<PaymentReceiptResponse> getReceipt(@PathVariable String paymentId) {
        PaymentReceiptResponse response = paymentService.getReceipt(paymentId);
        return ApiResponse.success("영수증 조회가 성공적으로 완료되었습니다.", response);
    }

    @Profile({"dev", "staging", "local"})
    @PostMapping("/{paymentId}/mock/succeed")
    public ApiResponse<PaymentResponse> mockSucceedPayment(
            @PathVariable String paymentId,
            @RequestBody PaymentMockSucceedRequest request) {
        PaymentResponse response = paymentService.mockSucceedPayment(paymentId, request);
        return ApiResponse.success("모킹 결제 승인이 성공적으로 완료되었습니다.", response);
    }

    @Profile({"dev", "staging", "local"})
    @PostMapping("/{paymentId}/mock/fail")
    public ApiResponse<PaymentResponse> mockFailPayment(
            @PathVariable String paymentId,
            @RequestBody PaymentMockFailRequest request) {
        PaymentResponse response = paymentService.mockFailPayment(paymentId, request);
        return ApiResponse.success("모킹 결제 거절이 성공적으로 완료되었습니다.", response);
    }
}
