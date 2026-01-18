package com.fusioncrew.aikiosk.domain.kiosk.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusioncrew.aikiosk.domain.kiosk.dto.EventRequest;
import com.fusioncrew.aikiosk.domain.kiosk.dto.SessionRequest;
import com.fusioncrew.aikiosk.domain.kiosk.entity.EndReason;
import com.fusioncrew.aikiosk.domain.kiosk.entity.KioskSession;
import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionEvent;
import com.fusioncrew.aikiosk.domain.kiosk.repository.SessionEventRepository;
import com.fusioncrew.aikiosk.domain.kiosk.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionEventRepository sessionEventRepository;
    private final ObjectMapper objectMapper;

    public KioskSession createSession(SessionRequest request) {
        String sessionId = generateSessionId();

        KioskSession session = KioskSession.builder()
                .sessionId(sessionId)
                .language(request.getLanguage())
                .largeText(request.getAccessibility().getLargeText())
                .highContrast(request.getAccessibility().getHighContrast())
                .voiceGuidance(request.getAccessibility().getVoiceGuidance())
                .inputMode(request.getInputMode())
                .kioskId(request.getDevice().getKioskId())
                .appVersion(request.getDevice().getAppVersion())
                .build();

        return sessionRepository.save(session);
    }

    private String generateSessionId() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = sessionRepository.count() + 1;
        return String.format("ses_%s_%04d", dateStr, count);
    }

    @Transactional(readOnly = true)
    public KioskSession getSession(String sessionId) {
        log.debug("Finding session by ID: {}", sessionId);
        return sessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + sessionId));
    }

    public KioskSession endSession(String sessionId, EndReason reason) {
        log.debug("Ending session: {} with reason: {}", sessionId, reason);
        KioskSession session = getSession(sessionId);
        session.endSession(reason);
        return sessionRepository.save(session);
    }

    public SessionEvent recordEvent(String sessionId, EventRequest request) {
        log.debug("Recording event for session: {} - type: {}", sessionId, request.getType());
        KioskSession session = getSession(sessionId);

        String payloadJson;
        try {
            payloadJson = objectMapper.writeValueAsString(request.getPayload());
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize event payload", e);
        }

        SessionEvent event = SessionEvent.builder()
                .kioskSession(session)
                .type(request.getType())
                .payload(payloadJson)
                .occurredAt(request.getOccurredAt().toLocalDateTime())
                .build();

        return sessionEventRepository.save(event);
    }

    public List<SessionEvent> getEventsBySessionId(String sessionId) {
        log.debug("Getting events for session: {}", sessionId);
        // Verify session existence
        getSession(sessionId);
        return sessionEventRepository.findAllByKioskSession_SessionId(sessionId);
    }
}
