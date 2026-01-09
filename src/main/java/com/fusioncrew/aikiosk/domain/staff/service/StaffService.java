package com.fusioncrew.aikiosk.domain.staff.service;

import com.fusioncrew.aikiosk.domain.staff.dto.StaffRequest;
import com.fusioncrew.aikiosk.domain.staff.entity.Staff;
import com.fusioncrew.aikiosk.domain.staff.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {

    private final StaffRepository staffRepository;

    public Staff submitCall(StaffRequest request) {
        Staff.StaffBuilder builder = Staff.builder()
                .sessionId(request.getSessionId())
                .reason(request.getReason())
                .message(request.getMessage());

        if (request.getContext() != null) {
            builder.screen(request.getContext().getScreen())
                    .orderId(request.getContext().getOrderId());
        }

        return staffRepository.save(builder.build());
    }
}
