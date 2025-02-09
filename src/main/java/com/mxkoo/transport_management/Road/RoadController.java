package com.mxkoo.transport_management.Road;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(RoadRoutes.ROOT)
public class RoadController {
    private RoadService roadService;

    @PostMapping(RoadRoutes.POST)
    public RoadDTO createRoad(@RequestBody @Valid RoadDTO roadDTO, @RequestParam int capacity){
        return roadService.createRoad(roadDTO, capacity);
    }

    @GetMapping(RoadRoutes.GET + "/{id}")
    public RoadDTO getRoadById(@PathVariable Long id){
        return roadService.getRoadById(id);
    }

    @GetMapping(RoadRoutes.GET + "/truckroads"+"/{truckId}")
    public List<RoadDTO> getAllTruckRoads(@PathVariable Long truckId){
        return roadService.getAllTruckRoads(truckId);
    }

    @GetMapping(RoadRoutes.GET + "/driverroads"+"/{driverId}")
    public List<RoadDTO> getAllDriverRoads(@PathVariable Long driverId){
        return roadService.getAllDriverRoads(driverId);
    }

    @GetMapping(RoadRoutes.GET + "/all")
    public List<RoadDTO> getAllRoads() {
        return roadService.getAllRoads();
    }

    @PatchMapping(RoadRoutes.UPDATE + "/{id}")
    public RoadDTO updateRoad(@PathVariable Long id, @RequestBody RoadDTO toUpdate){
        return roadService.updateRoad(id, toUpdate);
    }

    @DeleteMapping(RoadRoutes.DELETE + "/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllRoads(){
        roadService.deleteAllRoads();
    }
}
