package com.fusioncrew.aikiosk.domain.ticket.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "tickets", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ticket_order", columnNames = {"orderId"})
})
public class Ticket {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private LocalDate issuedDate;

    @Column(nullable = false)
    private Integer dailyNumber; // 오늘 대기번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TicketStatus status = TicketStatus.ISSUED;

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public LocalDate getIssuedDate() { return issuedDate; }
    public Integer getDailyNumber() { return dailyNumber; }
    public TicketStatus getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public void setIssuedDate(LocalDate issuedDate) { this.issuedDate = issuedDate; }
    public void setDailyNumber(Integer dailyNumber) { this.dailyNumber = dailyNumber; }
    public void setStatus(TicketStatus status) { this.status = status; }
}