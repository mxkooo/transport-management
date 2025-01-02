package com.mxkoo.transport_management.Location.LocationRoad;



import java.time.LocalDate;

public record LocationRoadDTO(
        String from,
        String[] via,
        String to,
        LocalDate departureDate,

        LocalDate arrivalDate,
        Long driverId,
        Long truckId
) {
}
