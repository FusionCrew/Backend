package com.fusioncrew.aikiosk.domain.kiosk.repository;

import com.fusioncrew.aikiosk.domain.kiosk.entity.KioskSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<KioskSession, Long> {

    Optional<KioskSession> findBySessionId(String sessionId);

    long countByKioskIdAndStatusAndCreatedAtBetween(
            String kioskId,
            com.fusioncrew.aikiosk.domain.kiosk.entity.SessionStatus status,
            java.time.LocalDateTime start,
            java.time.LocalDateTime end);
}
