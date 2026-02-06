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

    public StockService(StockRepository stockRepository,
            com.fusioncrew.aikiosk.domain.menu.repository.MenuItemRepository menuItemRepository) {
        this.stockRepository = stockRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<StockDtos.StockResponse> list() {
        return stockRepository.findAll()
                .stream()
                .map(StockDtos.StockResponse::from)
                .toList();
    }

    @Transactional
    public StockDtos.StockUpdateResponse upsert(StockDtos.StockUpsertRequest req) {
        if (req.menuItemId() == null || req.menuItemId().isBlank()) {
            throw new IllegalArgumentException("menuItemId is required");
        }

        menuItemRepository.findByMenuItemId(req.menuItemId())
                .orElseThrow(() -> new IllegalArgumentException("menu item not found"));

        Stock stock = stockRepository.findByIngredientId(req.menuItemId())
                .orElseGet(() -> new Stock(req.menuItemId(), 0));

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
    public StockDtos.StockUpdateResponse outOfStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));

        stock.markOutOfStock();
        Stock saved = stockRepository.save(stock);
        return StockDtos.StockUpdateResponse.from(saved);
    }
}