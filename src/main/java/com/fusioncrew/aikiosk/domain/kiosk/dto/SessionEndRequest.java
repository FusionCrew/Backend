package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.kiosk.entity.EndReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionEndRequest {

    @NotNull(message = "reason is required")
    private EndReason reason;
}
