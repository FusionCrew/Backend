package com.fusioncrew.aikiosk.domain.feedback.service;

import com.fusioncrew.aikiosk.domain.feedback.dto.FeedbackRequest;
import com.fusioncrew.aikiosk.domain.feedback.entity.Feedback;
import com.fusioncrew.aikiosk.domain.feedback.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public String submitFeedback(FeedbackRequest request) {
        Feedback feedback = Feedback.builder()
                .sessionId(request.getSessionId())
                .rating(request.getRating())
                .comment(request.getComment())
                .tags(request.getTags() != null ? request.getTags() : new java.util.ArrayList<>())
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return savedFeedback.getFormattedFeedbackId();
    }
}
