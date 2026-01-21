package com.fusioncrew.aikiosk.domain.ticket.entity;

import com.fusioncrew.aikiosk.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ticket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderId;

    @Column(nullable = false)
    private String paymentId;

    @Column(nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TicketStatus status = TicketStatus.WAITING;

    @Column(nullable = false)
    @Builder.Default
    private String priority = "NORMAL";

    private LocalDateTime calledAt;
    private LocalDateTime completedAt;

    public String getTicketId() {
        return String.format("tkt_%04d", this.id);
    }

    public void call() {
        this.status = TicketStatus.CALLED;
        this.calledAt = LocalDateTime.now();
    }

    public void complete() {
        this.status = TicketStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
