package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.Driver;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LeaveDTO(
        Long id,
        Driver driver,
        LocalDate start,
        LocalDate end
) {
}
