package com.fusioncrew.aikiosk.domain.stock.dto;

import com.fusioncrew.aikiosk.domain.stock.entity.Stock;

import java.time.OffsetDateTime;

public class StockDtos {

    public record StockResponse(
            Long stockId,
            String ingredientId,
            int quantity,
            boolean outOfStock,
            OffsetDateTime updatedAt
    ) {
        public static StockResponse from(Stock s) {
            return new StockResponse(
                    s.getId(),
                    s.getIngredientId(),
                    s.getQuantity(),
                    s.isOutOfStock(),
                    s.getUpdatedAt()
            );
        }
    }

    // POST /api/v1/admin/stocks (등록/초기화)
    public record StockUpsertRequest(
            String ingredientId,
            int quantity
    ) {}

    // PATCH /api/v1/admin/stocks/{stockId} (입고/차감)
    public record StockAdjustRequest(
            int delta
    ) {}

    // 공통 업데이트 응답
    public record StockUpdateResponse(
            Long stockId,
            String ingredientId,
            int quantity,
            boolean outOfStock
    ) {
        public static StockUpdateResponse from(Stock s) {
            return new StockUpdateResponse(s.getId(), s.getIngredientId(), s.getQuantity(), s.isOutOfStock());
        }
    }
}