package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.admin.dto.AdminStockUpdateRequest;
import com.fusioncrew.aikiosk.domain.admin.dto.AdminStockUpdateResponse;
import com.fusioncrew.aikiosk.domain.admin.service.AdminStockService;
import com.fusioncrew.aikiosk.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/stocks")
public class AdminStockController {

    private final AdminStockService adminStockService;

    @PatchMapping("/{stockId}")
    public ApiResponse<AdminStockUpdateResponse> updateStock(
            @PathVariable String stockId,
            @RequestBody AdminStockUpdateRequest req
    ) {
        AdminStockUpdateResponse data = adminStockService.updateStock(stockId, req);
        
        return ApiResponse.success("재고 수량 변경 성공", data);
    }
}