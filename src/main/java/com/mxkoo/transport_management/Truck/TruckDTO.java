package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.Builder;

import java.util.Date;

@Builder
public record TruckDTO(
        Long id,
        String licensePlate,
        Integer capacity,
        Coordinates coordinates,
        Date inspectionDate
) {
}
