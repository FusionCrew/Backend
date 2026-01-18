package com.fusioncrew.aikiosk.domain.stock.controller;

import com.fusioncrew.aikiosk.domain.stock.dto.StockDtos;
import com.fusioncrew.aikiosk.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/stocks")
@RequiredArgsConstructor
public class StockAdminController {

    private final StockService stockService;

    // GET /api/v1/admin/stocks
    @GetMapping
    public Map<String, Object> list() {
        List<StockDtos.StockResponse> items = stockService.list();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", Map.of("items", items));
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return response;
    }

    // POST /api/v1/admin/stocks (등록/초기화)
    @PostMapping
    public Map<String, Object> upsert(@RequestBody StockDtos.StockUpsertRequest req) {
        StockDtos.StockUpdateResponse dto = stockService.upsert(req);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", dto);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return response;
    }

    // PATCH /api/v1/admin/stocks/{stockId} (입고/차감)
    @PatchMapping("/{stockId}")
    public Map<String, Object> adjust(@PathVariable Long stockId,
                                      @RequestBody StockDtos.StockAdjustRequest req) {
        StockDtos.StockUpdateResponse dto = stockService.adjust(stockId, req);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", dto);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return response;
    }

    // POST /api/v1/admin/stocks/{stockId}/out-of-stock
    @PostMapping("/{stockId}/out-of-stock")
    public Map<String, Object> outOfStock(@PathVariable Long stockId) {
        StockDtos.StockUpdateResponse dto = stockService.outOfStock(stockId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", dto);
        response.put("timestamp", LocalDateTime.now());
        response.put("requestId", "req_" + UUID.randomUUID().toString().substring(0, 8));
        return response;
    }
}