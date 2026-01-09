package com.fusioncrew.aikiosk.domain.staff.repository;

import com.fusioncrew.aikiosk.domain.staff.entity.StaffCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffCallRepository extends JpaRepository<StaffCall, Long> {
}
