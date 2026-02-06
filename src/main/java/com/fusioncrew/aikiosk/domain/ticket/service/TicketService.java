package com.fusioncrew.aikiosk.domain.ticket.service;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import com.fusioncrew.aikiosk.domain.ticket.dto.TicketDtos;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.entity.TicketStatus;
import com.fusioncrew.aikiosk.domain.ticket.repository.TicketRepository;
import com.fusioncrew.aikiosk.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public TicketDtos.TicketResponse issue(TicketDtos.IssueTicketRequest req) {

        if (ticketRepository.existsByPaymentId(req.paymentId())) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 대기표가 발급된 결제입니다.");
        }

        Payment payment = paymentRepository.findById(parsePaymentId(req.paymentId()))
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보 없음"));

        if (payment.getStatus() != PaymentStatus.APPROVED) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제가 완료되지 않았습니다.");
        }

        if (!payment.getOrderId().equals(req.orderId())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "주문/결제 정보 불일치");
        }

        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        int number = (int) ticketRepository.countByCreatedAtBetween(start, end) + 1;

        Ticket ticket = Ticket.builder()
                .orderId(req.orderId())
                .paymentId(req.paymentId())
                .number(number)
                .status(TicketStatus.WAITING)
                .build();

        Ticket saved = ticketRepository.save(ticket);

        return new TicketDtos.TicketResponse(
                saved.getTicketId(),
                saved.getNumber(),
                saved.getStatus()
        );
    }

    public TicketDtos.TicketDetailResponse getDetail(String ticketId) {
        Long id = parseTicketId(ticketId);

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "티켓 없음"));

        int estimatedWaitMin = ticketRepository.countByStatus(TicketStatus.WAITING) * 2;

        return new TicketDtos.TicketDetailResponse(
                ticket.getTicketId(),
                ticket.getNumber(),
                ticket.getStatus(),
                estimatedWaitMin
        );
    }

    private Long parseTicketId(String ticketId) {
        return Long.parseLong(ticketId.replace("tkt_", ""));
    }

    private Long parsePaymentId(String paymentId) {
        return Long.parseLong(paymentId.replace("pay_", ""));
    }
}