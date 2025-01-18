package com.mxkoo.transport_management.Location.LocationDriver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;

public record LocationDriverDTO(
        Long id,
        Coordinates coordinates,
        String name,
        String lastName,
        Long contactNumber,
        DriverStatus driverStatus
) {
}
