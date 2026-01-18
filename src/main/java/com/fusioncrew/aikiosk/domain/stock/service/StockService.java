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
    private final IngredientRepository ingredientRepository;

    public StockService(StockRepository stockRepository, IngredientRepository ingredientRepository) {
        this.stockRepository = stockRepository;
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

        ingredientRepository.findByIngredientId(req.ingredientId())
                .orElseThrow(() -> new IllegalArgumentException("ingredient not found"));

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
    public StockDtos.StockUpdateResponse outOfStock(Long stockId) {
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("stock not found"));

        stock.markOutOfStock();
        Stock saved = stockRepository.save(stock);
        return StockDtos.StockUpdateResponse.from(saved);
    }
}