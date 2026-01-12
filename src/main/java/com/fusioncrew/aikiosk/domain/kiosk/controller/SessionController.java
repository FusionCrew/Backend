package com.fusioncrew.aikiosk.domain.kiosk.controller;

import com.fusioncrew.aikiosk.domain.kiosk.dto.*;
import com.fusioncrew.aikiosk.domain.kiosk.entity.KioskSession;
import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionEvent;
import com.fusioncrew.aikiosk.domain.kiosk.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/kiosk")
@RequiredArgsConstructor
public class SessionController {

        private final SessionService sessionService;

        @PostMapping("/sessions")
        public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody SessionRequest request) {
                KioskSession session = sessionService.createSession(request);

                String createdAtStr = session.getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                SessionResponse response = SessionResponse.builder()
                                .success(true)
                                .data(SessionResponse.SessionData.builder()
                                                .sessionId(session.getSessionId())
                                                .status(session.getStatus())
                                                .createdAt(createdAtStr)
                                                .build())
                                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                                .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/sessions/{sessionId}")
        public ResponseEntity<SessionDetailResponse> getSession(@PathVariable String sessionId) {
                KioskSession session = sessionService.getSession(sessionId);

                String startedAtStr = session.getCreatedAt()
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                String endedAtStr = session.getEndedAt() != null
                                ? session.getEndedAt().atZone(ZoneId.systemDefault())
                                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                                : null;

                SessionDetailResponse response = SessionDetailResponse.builder()
                                .success(true)
                                .data(SessionDetailResponse.SessionDetailData.builder()
                                                .sessionId(session.getSessionId())
                                                .status(session.getStatus())
                                                .language(session.getLanguage())
                                                .inputMode(session.getInputMode())
                                                .startedAt(startedAtStr)
                                                .endedAt(endedAtStr)
                                                .build())
                                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                                .build();

                return ResponseEntity.ok(response);
        }

        @PatchMapping("/sessions/{sessionId}/end")
        public ResponseEntity<SessionEndResponse> endSession(
                        @PathVariable String sessionId,
                        @Valid @RequestBody SessionEndRequest request) {
                KioskSession session = sessionService.endSession(sessionId, request.getReason());

                String endedAtStr = session.getEndedAt()
                                .atZone(ZoneId.systemDefault())
                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                SessionEndResponse response = SessionEndResponse.builder()
                                .success(true)
                                .data(SessionEndResponse.SessionEndData.builder()
                                                .sessionId(session.getSessionId())
                                                .status(session.getStatus())
                                                .endedAt(endedAtStr)
                                                .build())
                                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .build();
                return ResponseEntity.ok(response);
        }

        @PostMapping("/sessions/{sessionId}/events")
        public ResponseEntity<EventResponse> recordEvent(
                        @PathVariable String sessionId,
                        @Valid @RequestBody EventRequest request) {
                SessionEvent event = sessionService.recordEvent(sessionId, request);

                EventResponse response = EventResponse.builder()
                                .success(true)
                                .data(EventResponse.EventData.builder()
                                                .eventId("evt_" + event.getId())
                                                .sessionId(sessionId)
                                                .build())
                                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                                .build();

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping("/sessions/{sessionId}/events")
        public ResponseEntity<EventListResponse> getEvents(@PathVariable String sessionId) {
                List<SessionEvent> events = sessionService.getEventsBySessionId(sessionId);

                List<EventListResponse.EventItem> items = events.stream()
                                .map(event -> EventListResponse.EventItem.builder()
                                                .eventId("evt_" + event.getId())
                                                .type(event.getType())
                                                .occurredAt(event.getOccurredAt()
                                                                .atZone(ZoneId.systemDefault())
                                                                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                                .build())
                                .collect(Collectors.toList());

                EventListResponse response = EventListResponse.builder()
                                .success(true)
                                .data(EventListResponse.EventListData.builder()
                                                .sessionId(sessionId)
                                                .items(items)
                                                .build())
                                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                                .build();

                return ResponseEntity.ok(response);
        }
}
