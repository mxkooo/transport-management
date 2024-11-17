package com.mxkoo.transport_management.RoadStatus;

import com.mxkoo.transport_management.Road.Road;

import java.time.LocalDate;

public interface RoadStatusService {
    void setStatusForRoad(LocalDate departureDate, LocalDate arrivalDate, Road road);
}
