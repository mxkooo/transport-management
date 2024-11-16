package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Coordinates.Coordinates;

import java.util.List;

public interface TruckService {
    TruckDTO createTruck(TruckDTO truckDTO);
    TruckDTO getTruckById(Long id) throws Exception;
    List<TruckDTO> getAllTrucks();
    void deleteById(Long id) throws Exception;
    TruckDTO updateTruck(Long id, TruckDTO toUpdate) throws Exception;
    void setCoordinatesForTruck(Long truckId, Coordinates coordinates) throws Exception;
    Truck getAvailableTruck(int capacity);
}
