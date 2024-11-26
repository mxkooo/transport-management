package com.mxkoo.transport_management.Driver;


import com.mxkoo.transport_management.Coordinates.Coordinates;

import java.util.List;

public interface DriverService {
    DriverDTO createDriver(DriverDTO driverDTO);
    DriverDTO getDriverById(Long id) throws Exception;
    List<DriverDTO> getAllDrivers();
    void deleteById(Long id) throws Exception;
    DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception;
    DriverDTO setCoordinatesForDriver(Long driverId, Coordinates coordinates) throws Exception;
    Driver getAvailableDriver();
    void deleteAllDrivers();

}
