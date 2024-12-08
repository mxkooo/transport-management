package com.mxkoo.transport_management.Road;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoadRepository extends JpaRepository<Road, Long> {
    Optional<Road> findFirstByDriverIdOrderByDepartureDateAsc(Long driverId);
}
