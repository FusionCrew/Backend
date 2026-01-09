package com.fusioncrew.aikiosk.domain.staff.repository;

import com.fusioncrew.aikiosk.domain.staff.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {
}
