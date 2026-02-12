package com.fusioncrew.aikiosk.domain.stock.service;

import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import com.fusioncrew.aikiosk.domain.stock.dto.StockDtos;
import com.fusioncrew.aikiosk.domain.stock.entity.Stock;
import com.fusioncrew.aikiosk.domain.stock.repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;

    public StockService(StockRepository stockRepository,
            com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository menuItemRepository,
            IngredientRepository ingredientRepository) {
        this.stockRepository = stockRepository;
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<StockDtos.StockResponse> list() {
        return stockRepository.findAll()
                .stream()
                .map(StockDtos.StockResponse::from)
                .toList();
    }

    @Transactional
    public StockDtos.StockUpdateResponse upsert(StockDtos.StockUpsertRequest req) {
        if (req.ingredientId() == null || req.ingredientId().isBlank()) {
            throw new IllegalArgumentException("ingredientId is required");
        }

        // ingredientId가 ing_로 시작하면 ingredient로 검색, 그 외에는 menuItem으로 검색 (호환성 유지)
        if (req.ingredientId().startsWith("ing_")) {
            ingredientRepository.findByIngredientId(req.ingredientId())
                    .orElseThrow(() -> new IllegalArgumentException("ingredient not found: " + req.ingredientId()));
        } else {
            menuItemRepository.findByMenuItemId(req.ingredientId())
                    .orElseThrow(() -> new IllegalArgumentException("menu item not found: " + req.ingredientId()));
        }

        Stock stock = stockRepository.findByIngredientId(req.ingredientId())
                .orElseGet(() -> new Stock(req.ingredientId(), 0));

        stock.setQuantity(req.quantity());
        Stock saved = stockRepository.save(stock);
        return StockDtos.StockUpdateResponse.from(saved);
    }

    @Transactional
    public StockDtos.StockUpdateResponse adjust(Long stockId, StockDtos.StockAdjustRequest req) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));

        stock.applyDelta(req.delta());
        Stock saved = stockRepository.save(stock);
        return StockDtos.StockUpdateResponse.from(saved);
    }

    @Transactional
    public StockDtos.AdminStockOutOfStockResponse updateOutOfStock(Long stockId, StockDtos.StockOutOfStockRequest req) {
        if (req == null || req.isOutOfStock() == null) {
            throw new IllegalArgumentException("isOutOfStock is required");
        }

        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));

        stock.setOutOfStock(req.isOutOfStock());
        Stock saved = stockRepository.save(stock);
        return new StockDtos.AdminStockOutOfStockResponse(
                String.format("stk_%02d", saved.getId()),
                saved.isOutOfStock());
    }
}