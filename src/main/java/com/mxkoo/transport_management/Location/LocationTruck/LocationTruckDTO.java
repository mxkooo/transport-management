package com.mxkoo.transport_management.Location.LocationTruck;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;

public record LocationTruckDTO(
        Long id,
        Coordinates coordinates,
        String licensePlate,
        TruckStatus truckStatus
) {
}
