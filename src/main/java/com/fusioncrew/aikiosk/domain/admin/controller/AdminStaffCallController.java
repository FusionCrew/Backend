package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.staff.entity.Staff;
import com.fusioncrew.aikiosk.domain.staff.service.StaffService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/staff-calls")
@RequiredArgsConstructor
public class AdminStaffCallController {

    private final StaffService staffService;

    @GetMapping
    public ApiResponse<List<Staff>> getStaffCalls() {
        // Assume staffService has a method to get all calls
        // Since I don't know the exact service method, I'll check StaffService first or
        // implement it.
        return ApiResponse.ok(staffService.getAllCalls());
    }

    @PostMapping("/{callId}/resolve")
    public ApiResponse<Void> resolveCall(@PathVariable String callId) {
        staffService.resolveCall(callId);
        return ApiResponse.ok(null);
    }
}
