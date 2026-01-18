package com.fusioncrew.aikiosk.domain.stock.repository;

import com.fusioncrew.aikiosk.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByIngredientId(String ingredientId);
}