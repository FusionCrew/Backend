package com.fusioncrew.aikiosk.domain.kiosk.repository;

import com.fusioncrew.aikiosk.domain.kiosk.entity.SessionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionEventRepository extends JpaRepository<SessionEvent, Long> {
    List<SessionEvent> findAllByKioskSession_SessionId(String sessionId);
}
