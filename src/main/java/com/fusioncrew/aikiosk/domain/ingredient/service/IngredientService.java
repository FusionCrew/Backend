package com.fusioncrew.aikiosk.domain.ingredient.service;

import com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientCreateRequestDto;
import com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientResponseDto;
import com.fusioncrew.aikiosk.domain.ingredient.entity.Ingredient;
import com.fusioncrew.aikiosk.domain.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    // --- 기존 조회 메서드 유지 ---
    public List<IngredientResponseDto> getIngredientList() {
        return ingredientRepository.findAllByOrderByIngredientIdAsc().stream()
                .map(IngredientResponseDto::new)
                .collect(Collectors.toList());
    }

    // --- [New] 재료 등록 메서드 ---
    @Transactional
    public String createIngredient(IngredientCreateRequestDto requestDto) {
        // 1. ID 생성 로직 (예: ing_ + UUID 8자리)
        // 실제 운영에선 DB 시퀀스나 AtomicLong을 쓰기도 하지만, 여기선 UUID로 간단히 구현
        String generatedId = "ing_" + UUID.randomUUID().toString().substring(0, 8);

        // 2. 중복 체크 (혹시나 겹칠 경우 대비)
        while (ingredientRepository.existsByIngredientId(generatedId)) {
            generatedId = "ing_" + UUID.randomUUID().toString().substring(0, 8);
        }

        // 3. Entity 변환 및 저장
        Ingredient ingredient = requestDto.toEntity(generatedId);
        Ingredient saved = ingredientRepository.save(ingredient);

        // 4. 생성된 ID 반환
        return saved.getIngredientId();
    }

    // [New] 단일 재료 조회
    public Ingredient getIngredientDetail(String ingredientId) {
        return ingredientRepository.findByIngredientId(ingredientId)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("재료를 찾을 수 없습니다: " + ingredientId));
    }

    // [New] 재료 수정 (PATCH)
    @Transactional
    public Ingredient updateIngredient(String ingredientId,
            com.fusioncrew.aikiosk.domain.ingredient.dto.IngredientUpdateRequestDto requestDto) {
        Ingredient ingredient = getIngredientDetail(ingredientId);
        ingredient.updateDetails(
                requestDto.getName(),
                requestDto.getAllergyTag(),
                requestDto.getCalories(),
                requestDto.getExtraPrice());
        return ingredient;
    }

    // [New] 재료 삭제
    @Transactional
    public void deleteIngredient(String ingredientId) {
        Ingredient ingredient = getIngredientDetail(ingredientId);
        // 메뉴에서 사용 중인지 확인은 Controller 레벨에서 처리하거나,
        // 여기서 MenuItemRepository를 주입받아 체크할 수 있음
        // 현재는 간단한 물리 삭제로 구현 (과제용)
        ingredientRepository.delete(ingredient);
    }
}
