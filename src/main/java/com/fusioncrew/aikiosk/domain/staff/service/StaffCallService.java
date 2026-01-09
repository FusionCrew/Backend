package com.fusioncrew.aikiosk.domain.staff.service;

import com.fusioncrew.aikiosk.domain.staff.dto.StaffCallRequest;
import com.fusioncrew.aikiosk.domain.staff.entity.StaffCall;
import com.fusioncrew.aikiosk.domain.staff.repository.StaffCallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffCallService {

    private final StaffCallRepository staffCallRepository;

    public StaffCall submitCall(StaffCallRequest request) {
        StaffCall.StaffCallBuilder builder = StaffCall.builder()
                .sessionId(request.getSessionId())
                .reason(request.getReason())
                .message(request.getMessage());

        if (request.getContext() != null) {
            builder.screen(request.getContext().getScreen())
                    .orderId(request.getContext().getOrderId());
        }

        return staffCallRepository.save(builder.build());
    }
}
