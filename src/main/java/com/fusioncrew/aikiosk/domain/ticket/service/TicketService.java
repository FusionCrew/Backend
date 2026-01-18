package com.fusioncrew.aikiosk.domain.ticket.service;

import com.fusioncrew.aikiosk.domain.payment.entity.Payment;
import com.fusioncrew.aikiosk.domain.payment.entity.PaymentStatus;
import com.fusioncrew.aikiosk.domain.payment.repository.PaymentRepository;
import com.fusioncrew.aikiosk.domain.ticket.dto.TicketCreateRequest;
import com.fusioncrew.aikiosk.domain.ticket.dto.TicketResponse;
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

    @Transactional
    public TicketResponse createTicket(TicketCreateRequest request) {
        // 1. 중복 발급 체크
        if (ticketRepository.existsByPaymentId(request.getPaymentId())) {
            throw new CustomException(HttpStatus.CONFLICT, "이미 대기표가 발급된 결제 건입니다.");
        }

        long paymentIdLong;
        try {
            paymentIdLong = Long.parseLong(request.getPaymentId().replace("pay_", "").trim());
        } catch (NumberFormatException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "잘못된 결제 ID 형식입니다.");
        }

        // 2. 결제 정보 검증
        Payment payment = paymentRepository.findById(paymentIdLong)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."));

        if (!payment.getStatus().equals(PaymentStatus.APPROVED)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "결제가 완료되지 않은 주문입니다.");
        }

        if (!payment.getOrderId().equals(request.getOrderId())) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "주문 정보와 결제 정보가 일치하지 않습니다.");
        }

        // 3. 오늘 생성된 티켓 수 조회하여 번호 생성 (1부터 시작)
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        long countToday = ticketRepository.countByCreatedAtBetween(startOfDay, endOfDay);
        int nextNumber = (int) countToday + 1;

        // 4. 티켓 생성 및 저장
        Ticket ticket = Ticket.builder()
                .orderId(request.getOrderId())
                .paymentId(request.getPaymentId())
                .number(nextNumber)
                .status(TicketStatus.WAITING)
                .build();

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponse.builder()
                .ticketId(savedTicket.getTicketId())
                .number(savedTicket.getNumber())
                .status(savedTicket.getStatus())
                .build();
    }
}
