package com.mxkoo.transport_management.Location.LocationRoad;

import java.util.List;

public interface LocationRoadService {
    List<LocationRoadDTO> getRoadLocation();
    void updateRoadDistance(Long id, Double distance, Double price);
}
