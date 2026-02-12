package com.fusioncrew.aikiosk.domain.stock.init;

import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import com.fusioncrew.aikiosk.domain.stock.entity.Stock;
import com.fusioncrew.aikiosk.domain.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(2)
@RequiredArgsConstructor
public class StockDataInitializer implements CommandLineRunner {

    private final IngredientRepository ingredientRepository;
    private final StockRepository stockRepository;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            List<Ingredient> ingredients = ingredientRepository.findAll();
            int createdCount = 0;

            for (Ingredient ingredient : ingredients) {
                if (stockRepository.findByIngredientId(ingredient.getIngredientId()).isEmpty()) {
                    Stock stock = new Stock(ingredient.getIngredientId(), 90); // Initialize with 90 as per user
                                                                               // perception
                    stockRepository.save(stock);
                    createdCount++;
                }
            }

            if (createdCount > 0) {
                System.out.println("✅ " + createdCount + "개의 초기 재고 데이터가 생성되었습니다.");
            }
        } catch (Exception e) {
            System.err.println("❌ 재고 데이터 초기화 실패 (무시하고 계속 진행): " + e.getMessage());
        }
    }
}
