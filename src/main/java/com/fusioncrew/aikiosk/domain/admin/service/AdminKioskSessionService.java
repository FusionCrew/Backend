package com.fusioncrew.aikiosk.domain.admin.service;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminKioskSessionDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminKioskSessionListResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminSessionEventDetailResponse;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminSessionEventListResponse;
import com.fusioncrew.aikiosk.domain.kiosk.entity.EventType;
import com.fusioncrew.aikiosk.domain.kiosk.entity.KioskSession;
import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionEvent;
import com.fusioncrew.aikiosk.domain.kiosk.repository.KioskSessionRepository;
import com.fusioncrew.aikiosk.domain.kiosk.repository.SessionEventRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminKioskSessionService {

    private final KioskSessionRepository kioskSessionRepository;
    private final SessionEventRepository sessionEventRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public AdminKioskSessionListResponse getKioskSessionList(String from, String to) {
        LocalDateTime startDateTime = LocalDate.parse(from).atStartOfDay();
        LocalDateTime endDateTime = LocalDate.parse(to).atTime(LocalTime.MAX);

        List<KioskSession> sessions = kioskSessionRepository.findAllByDateRange(startDateTime, endDateTime);

        List<AdminKioskSessionListResponse.SessionItem> items = sessions.stream().map(session -> {
            long durationSec = 0;
            if (session.getEndedAt() != null) {
                durationSec = java.time.Duration.between(session.getCreatedAt(), session.getEndedAt()).getSeconds();
            } else {
                durationSec = java.time.Duration.between(session.getCreatedAt(), LocalDateTime.now()).getSeconds();
            }

            return AdminKioskSessionListResponse.SessionItem.builder()
                    .sessionId(session.getSessionId())
                    .kioskId(session.getKioskId())
                    .status(session.getStatus().name())
                    .startedAt(session.getCreatedAt())
                    .endedAt(session.getEndedAt())
                    .durationSec(durationSec)
                    .orderCount(0)
                    .totalAmount(BigDecimal.ZERO)
                    .paymentCount(0)
                    .canceledCount(0)
                    .build();
        }).collect(Collectors.toList());

        return AdminKioskSessionListResponse.builder()
                .items(items)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    @Transactional(readOnly = true)
    public AdminKioskSessionDetailResponse getKioskSessionDetail(String sessionId) {
        KioskSession session = kioskSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new com.fusioncrew.aikiosk.global.exception.CustomException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "세션 정보를 찾을 수 없습니다."));

        return AdminKioskSessionDetailResponse.builder()
                .sessionId(session.getSessionId())
                .language(session.getLanguage())
                .status(session.getStatus().name())
                .startedAt(session.getCreatedAt())
                .endedAt(session.getEndedAt())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    @Transactional(readOnly = true)
    public AdminSessionEventListResponse searchSessionEvents(String type, String from, String to, String sessionId) {
        LocalDateTime startDateTime = LocalDate.parse(from).atStartOfDay();
        LocalDateTime endDateTime = to != null ? LocalDate.parse(to).atTime(LocalTime.MAX) : LocalDateTime.now();

        EventType eventType = type != null ? EventType.valueOf(type) : null;

        List<SessionEvent> events = sessionEventRepository.searchEvents(eventType, sessionId, startDateTime,
                endDateTime);

        List<AdminSessionEventListResponse.EventItem> items = events.stream().map(event -> {
            Map<String, Object> metadata = null;
            try {
                metadata = objectMapper.readValue(event.getPayload(), new TypeReference<Map<String, Object>>() {
                });
            } catch (Exception e) {
                // ignore parse error
            }

            Long durationSeconds = null;
            String step = null;
            if (metadata != null) {
                if (metadata.containsKey("duration")) {
                    durationSeconds = ((Number) metadata.get("duration")).longValue();
                }
                if (metadata.containsKey("step")) {
                    step = (String) metadata.get("step");
                }
            }

            return AdminSessionEventListResponse.EventItem.builder()
                    .eventId("evt_" + event.getId())
                    .sessionId(event.getKioskSession().getSessionId())
                    .type(event.getType().name())
                    .occurredAt(event.getOccurredAt())
                    .durationSeconds(durationSeconds)
                    .step(step)
                    .metadata(metadata)
                    .kioskId(event.getKioskSession().getKioskId())
                    .build();
        }).collect(Collectors.toList());

        return AdminSessionEventListResponse.builder()
                .items(items)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }

    @Transactional(readOnly = true)
    public AdminSessionEventDetailResponse getSessionEventDetail(String eventId) {
        // Parse eventId (evt_123 -> 123)
        Long id = Long.parseLong(eventId.replace("evt_", ""));

        SessionEvent event = sessionEventRepository.findById(id)
                .orElseThrow(() -> new com.fusioncrew.aikiosk.global.exception.CustomException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "이벤트 정보를 찾을 수 없습니다."));

        Map<String, Object> payload = null;
        try {
            payload = objectMapper.readValue(event.getPayload(), new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            // ignore parse error
        }

        return AdminSessionEventDetailResponse.builder()
                .eventId("evt_" + event.getId())
                .sessionId(event.getKioskSession().getSessionId())
                .type(event.getType().name())
                .payload(payload)
                .occurredAt(event.getOccurredAt())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .requestId(UUID.randomUUID().toString().substring(0, 8))
                .build();
    }
}
