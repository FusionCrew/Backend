package com.fusioncrew.aikiosk.domain.kiosk.dto;

import com.fusioncrew.aikiosk.domain.menu.dto.MenuDetailKioskResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskMenuDetailResponse {
    private boolean success;
    private MenuDetailKioskResponseDto data;
    private String timestamp;
    private String requestId;
}
