package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    List<Driver> findDriverByDriverStatus(DriverStatus driverStatus);
}
