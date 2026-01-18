package com.fusioncrew.aikiosk.domain.kiosk.repository;

import com.fusioncrew.aikiosk.domain.kiosk.entity.EventType;
import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionEventRepository extends JpaRepository<SessionEvent, Long> {
    List<SessionEvent> findAllByKioskSession_SessionId(String sessionId);

    @Query("SELECT se FROM SessionEvent se " +
            "WHERE (:type IS NULL OR se.type = :type) " +
            "AND (:sessionId IS NULL OR se.kioskSession.sessionId = :sessionId) " +
            "AND se.occurredAt >= :from " +
            "AND se.occurredAt <= :to " +
            "ORDER BY se.occurredAt DESC")
    List<SessionEvent> searchEvents(
            @Param("type") EventType type,
            @Param("sessionId") String sessionId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
