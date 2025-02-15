package com.mxkoo.transport_management.Location;

import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverDTO;
import com.mxkoo.transport_management.Location.LocationDriver.LocationDriverService;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadDTO;
import com.mxkoo.transport_management.Location.LocationRoad.LocationRoadService;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckDTO;
import com.mxkoo.transport_management.Location.LocationTruck.LocationTruckService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/location")
@AllArgsConstructor
public class LocationController {
    private final LocationDriverService driverService;
    private final LocationTruckService truckService;
    private final LocationRoadService roadService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    @PostMapping
    public void receiveLocation(@RequestBody Map<String, Object> payload) {
        Long driverId = Long.valueOf(payload.get("driverId").toString());
        String x = payload.get("x").toString();
        String y = payload.get("y").toString();
        String timestamp = Instant.now().toString();

        String message = String.format("{\"driverId\": \"%s\", \"x\": %s, \"y\": %s, \"timestamp\": \"%s\"}",
                driverId, x, y, timestamp);

        kafkaTemplate.send("driver-location", driverId.toString(), message);
    }
    @GetMapping("/drivers")
    public List<LocationDriverDTO> getDrivers() {
        return driverService.getDriverLocations();
    }

    @GetMapping("/trucks")
    public List<LocationTruckDTO> getTrucks() {
        return truckService.getTruckLocations();
    }

    @GetMapping("/roads")
    public List<LocationRoadDTO> getRoads() {
        return roadService.getRoadLocation();
    }

    @GetMapping("/map")
    public String getMap(Model model) {
        List<LocationRoadDTO> roads = roadService.getRoadLocation();
        model.addAttribute("road", roads);
        return "map";
    }
}
