package com.mxkoo.transport_management.Truck;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    List<Truck> findByCapacityAndTruckStatus( int capacity, TruckStatus truckStatus);
}
