package com.fusioncrew.aikiosk.domain.staff.controller;

import com.fusioncrew.aikiosk.domain.staff.dto.StaffRequest;
import com.fusioncrew.aikiosk.domain.staff.dto.StaffResponse;
import com.fusioncrew.aikiosk.domain.staff.entity.Staff;
import com.fusioncrew.aikiosk.domain.staff.service.StaffService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kiosk/staff-calls")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    public ResponseEntity<StaffResponse> submitCall(@Valid @RequestBody StaffRequest request) {
        Staff savedCall = staffService.submitCall(request);

        StaffResponse response = StaffResponse.builder()
                .success(true)
                .data(StaffResponse.CallData.builder()
                        .callId(savedCall.getFormattedCallId())
                        .status(savedCall.getStatus())
                        .build())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
