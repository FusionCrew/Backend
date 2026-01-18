package com.fusioncrew.aikiosk.domain.kiosk.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "kiosk_session")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KioskSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @Column(nullable = false)
    private String language;

    @Column(nullable = false)
    private Boolean largeText;

    @Column(nullable = false)
    private Boolean highContrast;

    @Column(nullable = false)
    private Boolean voiceGuidance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InputMode inputMode;

    @Column(nullable = false)
    private String kioskId;

    @Column(nullable = false)
    private String appVersion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    @Column
    private EndReason endReason;

    @Builder
    public KioskSession(String sessionId, String language, Boolean largeText, Boolean highContrast,
            Boolean voiceGuidance, InputMode inputMode, String kioskId, String appVersion) {
        this.sessionId = sessionId;
        this.language = language;
        this.largeText = largeText;
        this.highContrast = highContrast;
        this.voiceGuidance = voiceGuidance;
        this.inputMode = inputMode;
        this.kioskId = kioskId;
        this.appVersion = appVersion;
        this.status = SessionStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public void endSession(EndReason reason) {
        this.status = SessionStatus.ENDED;
        this.endedAt = LocalDateTime.now();
        this.endReason = reason;
    }
}
