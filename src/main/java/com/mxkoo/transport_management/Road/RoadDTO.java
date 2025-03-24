package com.mxkoo.transport_management.Road;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mxkoo.transport_management.Driver.DriverDTO;
import com.mxkoo.transport_management.RoadStatus.RoadStatus;
import com.mxkoo.transport_management.Truck.TruckDTO;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record RoadDTO(
        Long id,
        String from,
        @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
