package com.mxkoo.transport_management.Location.LocationDriver;

import com.mxkoo.transport_management.Coordinates.Coordinates;

public record LocationDriverDTO(
        Long id,
        Coordinates coordinates,
        String name
) {
}
