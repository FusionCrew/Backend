package com.fusioncrew.aikiosk.domain.feedback.controller;

import com.fusioncrew.aikiosk.domain.feedback.dto.FeedbackRequest;
import com.fusioncrew.aikiosk.domain.feedback.dto.FeedbackResponse;
import com.fusioncrew.aikiosk.domain.feedback.service.FeedbackService;
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
@RequestMapping("/api/v1/kiosk/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackResponse> submitFeedback(@Valid @RequestBody FeedbackRequest request) {
        String feedbackId = feedbackService.submitFeedback(request);

        FeedbackResponse response = FeedbackResponse.builder()
                .success(true)
                .data(FeedbackResponse.FeedbackData.builder()
                        .feedbackId(feedbackId)
                        .build())
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
