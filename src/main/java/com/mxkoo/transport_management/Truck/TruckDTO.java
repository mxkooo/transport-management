package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.RoadDTO;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record TruckDTO(
        Long id,
        String licensePlate,
        Integer capacity,
        Coordinates coordinates,
        LocalDate inspectionDate,
        List<RoadDTO> roads,
        TruckStatus truckStatus
) {
}
