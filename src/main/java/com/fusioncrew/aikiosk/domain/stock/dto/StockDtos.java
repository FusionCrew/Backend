package com.fusioncrew.aikiosk.domain.stock.dto;

import com.fusioncrew.aikiosk.domain.stock.entity.Stock;

import java.time.OffsetDateTime;

public class StockDtos {

        public record StockResponse(
                        String stockId,
                        String ingredientId,
                        int quantity,
                        boolean outOfStock,
                        OffsetDateTime updatedAt) {
                public static StockResponse from(Stock s) {
                        return new StockResponse(
                                        String.format("stk_%02d", s.getId()),
                                        s.getIngredientId(),
                                        s.getQuantity(),
                                        s.isOutOfStock(),
                                        s.getUpdatedAt());
                }
        }

        // POST /api/v1/admin/stocks (등록/초기화)
        public record StockUpsertRequest(
                        String ingredientId,
                        int quantity) {
        }

        public record AdminStockUpsertResponse(
                        String stockId) {
        }

        // PATCH /api/v1/admin/stocks/{stockId} (입고/차감)
        public record StockAdjustRequest(
                        int delta) {
        }

        // POST /api/v1/admin/stocks/{stockId}/out-of-stock
        public record StockOutOfStockRequest(
                        Boolean isOutOfStock) {
        }

        public record AdminStockOutOfStockResponse(
                        String stockId,
                        boolean isOutOfStock) {
        }

        // 공통 업데이트 응답
        public record StockUpdateResponse(
                        String stockId,
                        int quantity,
                        boolean outOfStock) {
                public static StockUpdateResponse from(Stock s) {
                        return new StockUpdateResponse(
                                        String.format("stk_%02d", s.getId()),
                                        s.getQuantity(),
                                        s.isOutOfStock());
                }
        }
}