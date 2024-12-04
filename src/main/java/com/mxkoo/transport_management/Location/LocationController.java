package com.mxkoo.transport_management.Location;

import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverDTO;
import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverService;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckDTO;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {
    private final LocationDriverService driverService;
    private final LocationTruckService truckService;

    @GetMapping("/drivers")
    @ResponseBody
    public List<LocationDriverDTO> getDrivers() {
        return driverService.getDriverLocations();
    }

    @GetMapping("/trucks")
    @ResponseBody
    public List<LocationTruckDTO> getTrucks() {
        return truckService.getTruckLocations();
    }

    @GetMapping("/map")
    public String getMap() {
        return "map";
    }
}
