package com.fusioncrew.aikiosk.domain.admin.controller;

import com.fusioncrew.aikiosk.domain.stock.dto.StockDtos;
import com.fusioncrew.aikiosk.domain.stock.service.StockService;
import com.fusioncrew.aikiosk.global.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<StockDtos.AdminStockUpsertResponse> upsert(@RequestBody StockDtos.StockUpsertRequest req) {
        StockDtos.StockUpdateResponse dto = stockService.upsert(req);
        StockDtos.AdminStockUpsertResponse res = new StockDtos.AdminStockUpsertResponse(
                dto.stockId());
        return ApiResponse.ok(res);
    }

    // PATCH /api/v1/admin/stocks/{stockId} (입고/차감)
    @PatchMapping("/{stockId}")
    public ApiResponse<StockDtos.StockUpdateResponse> adjust(@PathVariable String stockId,
            @RequestBody StockDtos.StockAdjustRequest req) {
        Long id = parseStockId(stockId);
        StockDtos.StockUpdateResponse dto = stockService.adjust(id, req);
        return ApiResponse.ok(dto);
    }

    // POST /api/v1/admin/stocks/{stockId}/out-of-stock
    @PostMapping("/{stockId}/out-of-stock")
    public ApiResponse<StockDtos.AdminStockOutOfStockResponse> outOfStock(@PathVariable String stockId,
            @RequestBody StockDtos.StockOutOfStockRequest req) {
        Long id = parseStockId(stockId);
        StockDtos.AdminStockOutOfStockResponse dto = stockService.updateOutOfStock(id, req);
        return ApiResponse.ok(dto);
    }

    private Long parseStockId(String stockId) {
        if (stockId == null)
            return null;
        if (stockId.startsWith("stk_")) {
            return Long.parseLong(stockId.substring(4));
        }
        try {
            return Long.parseLong(stockId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid stockId format: " + stockId);
        }
    }
}