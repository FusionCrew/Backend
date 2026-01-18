package com.fusioncrew.aikiosk.domain.kiosk.repository;

import com.fusioncrew.aikiosk.domain.kiosk.entity.KioskSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface KioskSessionRepository extends JpaRepository<KioskSession, Long> {

    @Query("SELECT ks FROM KioskSession ks WHERE ks.createdAt >= :from AND ks.createdAt <= :to ORDER BY ks.createdAt DESC")
    List<KioskSession> findAllByDateRange(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    Optional<KioskSession> findBySessionId(String sessionId);
}
