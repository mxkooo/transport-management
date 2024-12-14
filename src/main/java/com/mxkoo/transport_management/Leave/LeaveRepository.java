package com.mxkoo.transport_management.Leave;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query("SELECT COUNT(l) FROM Leave l WHERE :date BETWEEN l.start AND l.end")
    int countActiveLeavesOnDate(@Param("date") LocalDate date);
    Optional<Leave> findFirstByDriverIdOrderByEndAsc(Long driverId);

}
