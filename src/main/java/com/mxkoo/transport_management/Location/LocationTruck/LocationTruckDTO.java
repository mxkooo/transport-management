package com.mxkoo.transport_management.Location.LocationTruck;

import com.mxkoo.transport_management.Coordinates.Coordinates;

public record LocationTruckDTO(
        Long id,
        Coordinates coordinates,
        String licensePlate
) {
}
