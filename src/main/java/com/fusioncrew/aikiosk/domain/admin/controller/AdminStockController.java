package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.stock.dto.StockDtos;
import com.fusioncrew.aikiosk.domain.stock.service.StockService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/stocks")
@RequiredArgsConstructor
public class AdminStockController {

    private final StockService stockService;

    // GET /api/v1/admin/stocks
    @GetMapping
    public ApiResponse<List<StockDtos.StockResponse>> list() {
        List<StockDtos.StockResponse> items = stockService.list();
        return ApiResponse.ok(items);
    }

    // POST /api/v1/admin/stocks (등록/초기화)
    @PostMapping
    public ApiResponse<StockDtos.StockUpdateResponse> upsert(@RequestBody StockDtos.StockUpsertRequest req) {
        StockDtos.StockUpdateResponse dto = stockService.upsert(req);
        return ApiResponse.ok(dto);
    }

    // PATCH /api/v1/admin/stocks/{stockId} (입고/차감)
    @PatchMapping("/{stockId}")
    public ApiResponse<StockDtos.StockUpdateResponse> adjust(@PathVariable Long stockId,
            @RequestBody StockDtos.StockAdjustRequest req) {
        StockDtos.StockUpdateResponse dto = stockService.adjust(stockId, req);
        return ApiResponse.ok(dto);
    }

    // POST /api/v1/admin/stocks/{stockId}/out-of-stock
    @PostMapping("/{stockId}/out-of-stock")
    public ApiResponse<StockDtos.StockUpdateResponse> outOfStock(@PathVariable Long stockId) {
        StockDtos.StockUpdateResponse dto = stockService.outOfStock(stockId);
        return ApiResponse.ok(dto);
    }
}