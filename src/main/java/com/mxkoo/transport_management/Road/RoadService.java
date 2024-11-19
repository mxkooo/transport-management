package com.mxkoo.transport_management.Road;

import java.util.List;

public interface RoadService {
    RoadDTO createRoad(RoadDTO roadDTO, int capacity);
    List<RoadDTO> getAllRoads();
    RoadDTO getRoadById(Long id);
}
