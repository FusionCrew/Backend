package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminPaymentDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminPaymentListResponse;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import com.fusioncrew.aikiosk.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPaymentService {

        private final PaymentRepository paymentRepository;
        private final OrderRepository orderRepository;

        public AdminPaymentListResponse getPaymentList(String status, String method, String orderId, String from,
                        String to, Integer page, Integer size) {
                // TODO: QueryDSL 등을 이용한 필터링 및 페이징 구현
                List<Payment> payments = paymentRepository.findAll();

                List<AdminPaymentListResponse.PaymentInfo> items = payments.stream()
                                .map(payment -> {
                                        Order order = orderRepository.findByOrderId(payment.getOrderId()).orElse(null);

                                        return AdminPaymentListResponse.PaymentInfo.builder()
                                                        .paymentId(payment.getPaymentId())
                                                        .orderId(payment.getOrderId())
                                                        .amount(payment.getAmount())
                                                        .currency(payment.getCurrency())
                                                        .status(payment.getStatus())
                                                        .method(payment.getMethod())
                                                        .isMock(payment.isMock())
                                                        .requestedAt(payment.getCreatedAt())
                                                        .approvedAt(payment
                                                                        .getStatus() == com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus.APPROVED
                                                                                        ? payment.getUpdatedAt()
                                                                                        : null)
                                                        .orderStatus(order != null ? order.getStatus() : null)
                                                        .buyer(AdminPaymentListResponse.BuyerInfo.builder()
                                                                        .type("KIOSK")
                                                                        .kioskId("kiosk_01")
                                                                        .build())
                                                        .failureReason(payment.getFailureReason())
                                                        .build();
                                })
                                .collect(Collectors.toList());

                return AdminPaymentListResponse.builder()
                                .items(items)
                                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                .requestId(UUID.randomUUID().toString().substring(0, 8))
                                .build();
        }

        public AdminPaymentDetailResponse getPaymentDetail(String paymentId) {
                Long id = parsePaymentId(paymentId);
                Payment payment = paymentRepository.findById(id)
                                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

                Order order = orderRepository.findByOrderId(payment.getOrderId()).orElse(null);

                BigDecimal total = payment.getAmount();
                BigDecimal supply = total.divide(new BigDecimal("1.1"), 0, RoundingMode.HALF_UP);
                BigDecimal tax = total.subtract(supply);

                AdminPaymentDetailResponse.OrderDetail orderDetail = null;
                if (order != null) {
                        orderDetail = AdminPaymentDetailResponse.OrderDetail.builder()
                                        .orderId(order.getOrderId())
                                        .status(order.getStatus())
                                        .totalQuantity(order.getItems().stream().mapToInt(item -> item.getQuantity())
                                                        .sum())
                                        .items(order.getItems().stream()
                                                        .map(item -> AdminPaymentDetailResponse.OrderItemDetail
                                                                        .builder()
                                                                        .menuItemId(item.getMenuItemId())
                                                                        .name(item.getName())
                                                                        .quantity(item.getQuantity())
                                                                        .price(item.getPrice())
                                                                        .build())
                                                        .collect(Collectors.toList()))
                                        .build();
                }

                return AdminPaymentDetailResponse.builder()
                                .payment(AdminPaymentDetailResponse.PaymentDetail.builder()
                                                .paymentId(payment.getPaymentId())
                                                .orderId(payment.getOrderId())
                                                .status(payment.getStatus())
                                                .method(payment.getMethod())
                                                .isMock(payment.isMock())
                                                .amount(AdminPaymentDetailResponse.AmountDetail.builder()
                                                                .total(total)
                                                                .currency(payment.getCurrency())
                                                                .tax(tax)
                                                                .supply(supply)
                                                                .build())
                                                .requestedAt(payment.getCreatedAt())
                                                .approvedAt(payment
                                                                .getStatus() == com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus.APPROVED
                                                                                ? payment.getUpdatedAt()
                                                                                : null)
                                                .failure(payment.getFailureReason() != null
                                                                ? AdminPaymentDetailResponse.FailureDetail.builder()
                                                                                .code("ERROR")
                                                                                .message(payment.getFailureReason())
                                                                                .occurredAt(payment.getUpdatedAt())
                                                                                .build()
                                                                : null)
                                                .cancel(null)
                                                .refund(payment.getRefundedAmount().compareTo(BigDecimal.ZERO) > 0
                                                                ? AdminPaymentDetailResponse.RefundDetail.builder()
                                                                                .amount(payment.getRefundedAmount())
                                                                                .refundedAt(payment.getUpdatedAt())
                                                                                .build()
                                                                : null)
                                                .build())
                                .order(orderDetail)
                                .pg(AdminPaymentDetailResponse.PgDetail.builder()
                                                .provider(payment.getPgProvider())
                                                .tid(payment.getPgTid())
                                                .rawStatus(payment.getStatus().name())
                                                .responseCode("0000")
                                                .responseMessage("OK")
                                                .build())
                                .buyer(AdminPaymentDetailResponse.BuyerDetail.builder()
                                                .type(payment.getBuyerType() != null ? payment.getBuyerType() : "KIOSK")
                                                .kioskId(payment.getKioskId() != null ? payment.getKioskId()
                                                                : "kiosk_01")
                                                .build())
                                .audit(AdminPaymentDetailResponse.AuditDetail.builder()
                                                .createdBy("SYSTEM")
                                                .createdAt(payment.getCreatedAt())
                                                .lastUpdatedAt(payment.getUpdatedAt())
                                                .build())
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
