package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.Builder;

@Builder
public record DriverDTO(
        Long id,
        String name,
        String lastName,
        Coordinates coordinates,
        String email,
        Long contactNumber
) {}
