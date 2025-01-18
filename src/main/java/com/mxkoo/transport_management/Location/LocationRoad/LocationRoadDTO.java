package com.mxkoo.transport_management.Location.LocationRoad;



import com.mxkoo.transport_management.RoadStatus.RoadStatus;

import java.time.LocalDate;

public record LocationRoadDTO(
        Long id,
        String from,
        String[] via,
        String to,
        LocalDate departureDate,

        LocalDate arrivalDate,
        Double distance,
        Double price,
        Long driverId,
        Long truckId,
        RoadStatus roadStatus
) {
}
