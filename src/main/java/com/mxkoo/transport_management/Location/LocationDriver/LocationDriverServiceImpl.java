package com.mxkoo.transport_management.Location.LocationDriver;

import com.mxkoo.transport_management.Driver.DriverRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationDriverServiceImpl implements LocationDriverService{
    private final DriverRepository driverRepository;

    public List<LocationDriverDTO> getDriverLocations() {
        return driverRepository.findAll()
                .stream()
                .map(driver -> new LocationDriverDTO(
                        driver.getId(),
                        driver.getCoordinates(),
                        driver.getName(),
                        driver.getLastName(),
                        driver.getContactNumber(),
                        driver.getDriverStatus()
                ))
                .collect(Collectors.toList());
    }
}
