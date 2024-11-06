package com.mxkoo.transport_management.Driver;


import java.util.List;

public interface DriverService {
    DriverDTO createDriver(DriverDTO driverDTO);
    DriverDTO getDriverById(Long id) throws Exception;
    List<DriverDTO> getAllDrivers();
    void deleteById(Long id) throws Exception;
    DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception;

}
