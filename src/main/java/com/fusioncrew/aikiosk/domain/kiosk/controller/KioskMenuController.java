package com.fusioncrew.aikiosk.domain.kiosk.controller;

import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskMenuListResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskMenuDetailResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskIngredientResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskRecommendationResponse;
import com.fusioncrew.aikiosk.domain.kiosk.dto.KioskCategoryResponse;
import com.fusioncrew.aikiosk.domain.kiosk.service.KioskMenuService;
import com.fusioncrew.aikiosk.domain.menu.dto.MenuDetailKioskResponseDto;
import com.fusioncrew.aikiosk.domain.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/kiosk")
@RequiredArgsConstructor
public class KioskMenuController {

    private final KioskMenuService kioskMenuService;
    private final MenuService menuService;

    @GetMapping("/menu-items")
    public ResponseEntity<KioskMenuListResponse> getMenuItems(
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "20") int size) {

        KioskMenuListResponse response = kioskMenuService.getKioskMenuItems(categoryId, keyword, cursor, size);

        response.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        response.setRequestId("req_" + UUID.randomUUID().toString().substring(0, 8));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/menu-items/{menuItemId}")
    public ResponseEntity<KioskMenuDetailResponse> getMenuDetail(@PathVariable String menuItemId) {
        MenuDetailKioskResponseDto data = menuService.getKioskMenuDetail(menuItemId);

        KioskMenuDetailResponse response = KioskMenuDetailResponse.builder()
                .success(true)
                .data(data)
                .timestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .requestId("req_" + UUID.randomUUID().toString().substring(0, 8))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<KioskCategoryResponse> getCategories() {
        KioskCategoryResponse response = kioskMenuService.getCategories();

        response.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        response.setRequestId("req_" + UUID.randomUUID().toString().substring(0, 8));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestParam(required = false) String sessionId) {
        // sessionId 검증: null, 빈 문자열, 공백만 있는 경우 400 에러
        if (sessionId == null || sessionId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(java.util.Map.of(
                    "success", false,
                    "error", java.util.Map.of(
                            "code", "INVALID_SESSION_ID",
                            "message", "sessionId는 필수 파라미터입니다."),
                    "timestamp", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                    "requestId", "req_" + UUID.randomUUID().toString().substring(0, 8)));
        }

        KioskRecommendationResponse response = kioskMenuService.getRecommendations(sessionId);

        response.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        response.setRequestId("req_" + UUID.randomUUID().toString().substring(0, 8));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ingredients")
    public ResponseEntity<KioskIngredientResponse> getIngredients() {
        KioskIngredientResponse response = kioskMenuService.getIngredientFilters();

        response.setTimestamp(ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        response.setRequestId("req_" + UUID.randomUUID().toString().substring(0, 8));

        return ResponseEntity.ok(response);
    }
}
