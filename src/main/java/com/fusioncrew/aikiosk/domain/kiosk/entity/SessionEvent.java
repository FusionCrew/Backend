package com.fusioncrew.aikiosk.domain.kiosk.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "session_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kiosk_session_id", nullable = false)
    private KioskSession kioskSession;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    @Builder
    public SessionEvent(KioskSession kioskSession, EventType type, String payload, LocalDateTime occurredAt) {
        this.kioskSession = kioskSession;
        this.type = type;
        this.payload = payload;
        this.occurredAt = occurredAt;
    }
}
