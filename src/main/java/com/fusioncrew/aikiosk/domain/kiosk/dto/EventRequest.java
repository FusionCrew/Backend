package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {

    @NotNull(message = "type is required")
    private EventType type;

    @NotNull(message = "payload is required")
    private Map<String, Object> payload;

    @NotNull(message = "occurredAt is required")
    private ZonedDateTime occurredAt;
}
