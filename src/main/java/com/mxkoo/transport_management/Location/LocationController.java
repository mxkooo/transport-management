package com.mxkoo.transport_management.Location;

import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverDTO;
import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverService;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadDTO;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadService;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckDTO;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {
    private final LocationDriverService driverService;
    private final LocationTruckService truckService;
    private final LocationRoadService roadService;

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

    @GetMapping("/roads")
    @ResponseBody
    public List<LocationRoadDTO> getRoads() {
        return roadService.getRoadLocation();
    }

    @PatchMapping("/road/distance/{id}")
    public ResponseEntity<String> updateDistance(@PathVariable Long id, @RequestParam Double distance, @RequestParam Double price) {
        try {
            roadService.updateRoadDistance(id, distance, price);
            return ResponseEntity.ok("Distance updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating distance: " + e.getMessage());
        }
    }

    @GetMapping("/map")
    public String getMap(Model model) {
        List<LocationRoadDTO> roads = roadService.getRoadLocation();
        model.addAttribute("road", roads);
        return "map";
    }
}
