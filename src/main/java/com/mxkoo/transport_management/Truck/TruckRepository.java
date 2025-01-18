package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    @Query("SELECT t FROM Truck t WHERE t.capacity >= :capacity AND t.truckStatus = :status")
    List<Truck> findByCapacityAndTruckStatus(
                @Param("capacity") double capacity,
                @Param("status") TruckStatus status);
}
