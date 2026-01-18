package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.*;
import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import com.fusioncrew.aikiosk.domain.ticket.repository.TicketRepository;
import com.fusioncrew.aikiosk.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminTicketService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public AdminTicketListResponse getTicketList() {
        List<Ticket> tickets = ticketRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        long total = tickets.size();
        long waiting = tickets.stream().filter(t -> t.getStatus() == TicketStatus.WAITING).count();
        long called = tickets.stream().filter(t -> t.getStatus() == TicketStatus.CALLED).count();
        long completed = tickets.stream().filter(t -> t.getStatus() == TicketStatus.COMPLETED).count();

        List<AdminTicketListResponse.TicketItem> items = tickets.stream().map(ticket -> {
            Order order = orderRepository.findByOrderId(ticket.getOrderId()).orElse(null);

            // Payment ID parsing logic duplicated here or just use repository findById if
            // ID is standard Long
            // Ticket stores "pay_xxxx". Need to parse.
            Long paymentId = parsePaymentId(ticket.getPaymentId());
            Payment payment = paymentRepository.findById(paymentId).orElse(null);

            long waitingSeconds = 0;
            if (ticket.getStatus() == TicketStatus.WAITING) {
                waitingSeconds = java.time.Duration.between(ticket.getCreatedAt(), LocalDateTime.now()).getSeconds();
            } else if (ticket.getCalledAt() != null) {
                waitingSeconds = java.time.Duration.between(ticket.getCreatedAt(), ticket.getCalledAt()).getSeconds();
            }

            return AdminTicketListResponse.TicketItem.builder()
                    .ticketId(ticket.getTicketId())
                    .number(ticket.getNumber())
                    .status(ticket.getStatus().name())
                    .priority(ticket.getPriority())
                    .orderId(ticket.getOrderId())
                    .orderStatus(order != null ? order.getStatus().name() : "UNKNOWN")
                    .kioskId(payment != null && payment.getKioskId() != null ? payment.getKioskId() : "kiosk_01")
                    .issuedAt(ticket.getCreatedAt())
                    .calledAt(ticket.getCalledAt())
                    .completedAt(ticket.getCompletedAt())
                    .waitingSeconds(waitingSeconds)
                    .build();
        }).collect(Collectors.toList());

        return AdminTicketListResponse.builder()
                .items(items)
                .summary(AdminTicketListResponse.TicketSummary.builder()
                        .total(total)
                        .waiting(waiting)
                        .called(called)
                        .completed(completed)
                        .build())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    private Long parsePaymentId(String paymentId) {
        try {
            return Long.parseLong(paymentId.replace("pay_", "").trim());
        } catch (Exception e) {
            return -1L;
        }
    }

    @Transactional
    public AdminTicketCallResponse callTicket(String ticketId, AdminTicketCallRequest request) {
        long id;
        try {
            id = Long.parseLong(ticketId.replace("tkt_", ""));
        } catch (NumberFormatException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 티켓 ID 형식입니다.");
        }

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "티켓 정보를 찾을 수 없습니다."));

        // 상태 업데이트
        ticket.call();

        // 추가 해야 할 것 : 호출 메시지(request.getMessage())를 이용한 알림 로직 (e.g. WebSocket,
        // NotificationService 등) 추가 가능
        // 현재는 DB 상태 변경만 수행

        return AdminTicketCallResponse.builder()
                .ticketId(ticket.getTicketId())
                .status(ticket.getStatus())
                .build();
    }

    @Transactional
    public AdminTicketServeResponse serveTicket(String ticketId, AdminTicketServeRequest request) {
        long id;
        try {
            id = Long.parseLong(ticketId.replace("tkt_", ""));
        } catch (NumberFormatException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 티켓 ID 형식입니다.");
        }

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "티켓 정보를 찾을 수 없습니다."));

        // 상태 업데이트
        ticket.complete();

        // TODO: servedBy 정보를 audit 로그에 남기거나 하는 로직 추가 가능

        return AdminTicketServeResponse.builder()
                .ticketId(ticket.getTicketId())
                .status(ticket.getStatus())
                .build();
    }
}
