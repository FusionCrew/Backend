package com.fusioncrew.aikiosk.domain.payment.service;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.payment.dto.*;
import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentMethod;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import com.fusioncrew.aikiosk.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .method(request.getMethod())
                .mock(request.isMock())
                .returnUrl(request.getReturnUrl())
                .status(PaymentStatus.CREATED)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        return convertToResponse(savedPayment, true);
    }

    @Transactional
    public PaymentResponse approvePayment(String paymentId, PaymentApproveRequest request) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.updateStatus(PaymentStatus.APPROVED);
        payment.setPgTid(request.getPg().getTid());
        payment.setPgProvider(request.getPg().getProvider());

        return convertToResponse(payment, false);
    }

    @Transactional
    public PaymentResponse declinePayment(String paymentId, PaymentDeclineRequest request) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.updateStatus(PaymentStatus.DECLINED);
        payment.setPgProvider(request.getPg().getProvider());

        return convertToResponse(payment, false);
    }

    @Transactional
    public PaymentResponse refundPayment(String paymentId, PaymentRefundRequest request) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        payment.refund(request.getAmount());

        return convertToResponse(payment, false);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPayment(String paymentId) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        return convertToResponse(payment, false);
    }

    @Transactional(readOnly = true)
    public PaymentMethodResponse getPaymentMethods() {
        List<PaymentMethodResponse.MethodInfo> methods = Arrays.stream(PaymentMethod.values())
                .map(m -> PaymentMethodResponse.MethodInfo.builder()
                        .code(m.name())
                        .displayName(m.getDisplayName())
                        .enabled(m.isEnabled())
                        .supportsRefund(m.isSupportsRefund())
                        .maxAmount(m.getMaxAmount())
                        .build())
                .toList();

        return PaymentMethodResponse.builder()
                .methods(methods)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    @Transactional
    public PaymentResponse mockSucceedPayment(String paymentId, PaymentMockSucceedRequest request) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        if (payment.getStatus() == PaymentStatus.APPROVED ||
                payment.getStatus() == PaymentStatus.DECLINED ||
                payment.getStatus() == PaymentStatus.REFUNDED ||
                payment.getStatus() == PaymentStatus.PARTIALLY_REFUNDED) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 처리된 결제입니다.");
        }

        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제 상태가 CREATED가 아닙니다.");
        }

        if (!payment.isMock()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "모킹 API 접근 권한이 없습니다. (실결제 건)");
        }

        payment.updateStatus(PaymentStatus.APPROVED);
        payment.setPgTid(request.getTid());
        payment.setPgProvider("MOCK");

        return convertToResponse(payment, false);
    }

    @Transactional
    public PaymentResponse mockFailPayment(String paymentId, PaymentMockFailRequest request) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        if (payment.getStatus() == PaymentStatus.APPROVED ||
                payment.getStatus() == PaymentStatus.DECLINED ||
                payment.getStatus() == PaymentStatus.REFUNDED ||
                payment.getStatus() == PaymentStatus.PARTIALLY_REFUNDED) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 처리된 결제입니다.");
        }

        if (payment.getStatus() != PaymentStatus.CREATED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제 상태가 CREATED가 아닙니다.");
        }

        if (!payment.isMock()) {
            throw new CustomException(HttpStatus.FORBIDDEN, "모킹 API 접근 권한이 없습니다. (실결제 건)");
        }

        payment.updateStatus(PaymentStatus.DECLINED);
        payment.setPgProvider("MOCK");

        return convertToResponse(payment, false);
    }

    @Transactional(readOnly = true)
    public PaymentReceiptResponse getReceipt(String paymentId) {
        Long id = parsePaymentId(paymentId);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        if (payment.getStatus() != PaymentStatus.APPROVED && payment.getStatus() != PaymentStatus.REFUNDED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "승인된 결제 건만 영수증 조회가 가능합니다.");
        }

        Order order = orderRepository.findByOrderId(payment.getOrderId())
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "주문 정보를 찾을 수 없습니다."));

        List<PaymentReceiptResponse.ReceiptItem> receiptItems = order.getItems().stream()
                .map(item -> PaymentReceiptResponse.ReceiptItem.builder()
                        .name(item.getName())
                        .qty(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        return PaymentReceiptResponse.builder()
                .paymentId(payment.getPaymentId())
                .receiptNo(
                        "rcp_" + payment.getPaymentId().replace("pay_", "") + "_" + System.currentTimeMillis() % 10000)
                .issuedAt(payment.getCreatedAt())
                .items(receiptItems)
                .total(payment.getAmount())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    private PaymentResponse convertToResponse(Payment payment, boolean includeRedirect) {
        return PaymentResponse.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .status(payment.getStatus())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .refundedAmount(payment.getRefundedAmount())
                .redirectUrl(includeRedirect ? payment.getRedirectUrl() : null)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    private Long parsePaymentId(String paymentId) {
        if (paymentId == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제 ID가 누락되었습니다.");
        }
        try {
            String cleanId = paymentId.toLowerCase().trim().replace("pay_", "");
            return Long.parseLong(cleanId);
        } catch (NumberFormatException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 결제 ID 형식입니다: " + paymentId);
        }
    }
}
