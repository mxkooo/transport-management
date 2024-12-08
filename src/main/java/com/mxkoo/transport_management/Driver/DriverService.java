package com.mxkoo.transport_management.Driver;


import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.RoadDTO;

import java.util.List;

public interface DriverService {
    DriverDTO createDriver(DriverDTO driverDTO);
    DriverDTO getDriverDTOById(Long id) throws Exception;
    Driver getDriverById(Long driverId) throws Exception;
    List<DriverDTO> getAllDrivers();
    void deleteById(Long id) throws Exception;
    DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception;
    DriverDTO setCoordinatesForDriver(Long driverId, Coordinates coordinates) throws Exception;
    void deleteAllDrivers();
    Driver getAvailableDriverNotOnRoad(RoadDTO road);
}
