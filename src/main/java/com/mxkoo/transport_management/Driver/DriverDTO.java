package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.Leave.Leave;
import com.mxkoo.transport_management.Road.RoadDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record DriverDTO(
        Long id,
        String name,
        String lastName,
        Coordinates coordinates,
        String email,
        Long contactNumber,
        List<RoadDTO> roads,
        DriverStatus driverStatus,
        int daysOffLeft,
        List<Leave> leaves
) {}
