package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.DriverDTO;
import com.mxkoo.transport_management.RoadStatus.RoadStatus;
import com.mxkoo.transport_management.Truck.TruckDTO;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record RoadDTO(
        Long id,
        String from,
        String[] via,
        String to,
        LocalDate departureDate,
        LocalDate arrivalDate,
        Double distance,
        Double price,
        TruckDTO truckDTO,
        DriverDTO driverDTO,
        RoadStatus roadStatus
) {
}
