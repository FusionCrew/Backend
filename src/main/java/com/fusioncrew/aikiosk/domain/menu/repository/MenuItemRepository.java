package com.fusioncrew.aikiosk.domain.menu.repository;

import com.fusioncrew.aikiosk.domain.menu.entity.MenuItem;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // 관리자용 목록 조회
    List<MenuItem> findAllByOrderByMenuItemIdAsc();

    // 메뉴 등록 시 ID 중복 체크용
    boolean existsByMenuItemId(String menuItemId);

    // [추가] 메뉴 상세 조회 및 수정용 (Service에서 호출 중)
    Optional<MenuItem> findByMenuItemId(String menuItemId);

    // [키오스크용] 필터링 및 커서 기반 조회
    @Query("SELECT m FROM MenuItem m WHERE m.hidden = false " +
            "AND (:categoryId IS NULL OR m.categoryId = :categoryId) " +
            "AND (:keyword IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:cursor IS NULL OR m.menuItemId > :cursor) " +
            "ORDER BY m.menuItemId ASC")
    List<MenuItem> findKioskMenuItems(
            @Param("categoryId") String categoryId,
            @Param("keyword") String keyword,
            @Param("cursor") String cursor,
            Pageable pageable);
}