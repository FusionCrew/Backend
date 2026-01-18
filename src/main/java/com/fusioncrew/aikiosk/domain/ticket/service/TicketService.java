package com.fusioncrew.aikiosk.domain.ticket.service;

import com.fusioncrew.aikiosk.domain.order.entity.Order;
import com.fusioncrew.aikiosk.domain.order.entity.OrderStatus;
import com.fusioncrew.aikiosk.domain.order.repository.OrderRepository;
import com.fusioncrew.aikiosk.domain.ticket.entity.Ticket;
import com.fusioncrew.aikiosk.domain.ticket.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final OrderRepository orderRepository;

    public TicketService(TicketRepository ticketRepository, OrderRepository orderRepository) {
        this.ticketRepository = ticketRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public Ticket issue(Long orderId) {
        if (orderId == null) throw new IllegalArgumentException("orderId is required");

        // 이미 발급된 티켓이 있으면 그대로 반환
        return ticketRepository.findByOrderId(orderId).orElseGet(() -> {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("order not found"));

            if (order.getStatus() != OrderStatus.CONFIRMED) {
                throw new IllegalArgumentException("ticket can be issued only for confirmed orders");
            }

            LocalDate today = LocalDate.now();
            int nextNumber = (int) ticketRepository.countByIssuedDate(today) + 1;

            Ticket t = new Ticket();
            t.setOrderId(orderId);
            t.setIssuedDate(today);
            t.setDailyNumber(nextNumber);
            return ticketRepository.save(t);
        });
    }

    public Ticket get(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("ticket not found"));
    }
}