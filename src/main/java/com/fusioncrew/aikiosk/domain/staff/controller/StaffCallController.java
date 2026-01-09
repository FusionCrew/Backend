package com.fusioncrew.aikiosk.domain.staff.controller;

import com.fusioncrew.aikiosk.domain.staff.dto.StaffCallRequest;
import com.fusioncrew.aikiosk.domain.staff.dto.StaffCallResponse;
import com.fusioncrew.aikiosk.domain.staff.entity.StaffCall;
import com.fusioncrew.aikiosk.domain.staff.service.StaffCallService;
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
@RequestMapping("/api/v1/staff/call")
@RequiredArgsConstructor
public class StaffCallController {

    private final StaffCallService staffCallService;

    @PostMapping
    public ResponseEntity<StaffCallResponse> submitCall(@Valid @RequestBody StaffCallRequest request) {
        StaffCall savedCall = staffCallService.submitCall(request);

        StaffCallResponse response = StaffCallResponse.builder()
                .success(true)
                .data(StaffCallResponse.CallData.builder()
                        .callId(savedCall.getFormattedCallId())
                        .status(savedCall.getStatus())
                        .build())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
