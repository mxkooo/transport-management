package com.mxkoo.transport_management.Road;

import java.util.List;

public interface RoadService {
    RoadDTO createRoad(RoadDTO roadDTO, int capacity);
    List<RoadDTO> getAllTruckRoads(Long truckId);
    List<RoadDTO> getAllDriverRoads(Long driverId);
    List<RoadDTO> getAllRoads();
    RoadDTO getRoadById(Long id);
    RoadDTO updateRoad(Long id, RoadDTO toUpdate);
    void deleteAllRoads();
}
