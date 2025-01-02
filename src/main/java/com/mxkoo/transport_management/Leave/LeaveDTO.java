package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.DriverDTO;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveDTO(
        Long id,
        DriverDTO driverDTO,
        LocalDate start,
        LocalDate end
) {
}
