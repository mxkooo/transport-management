package com.mxkoo.transport_management.Road;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(RoadRoutes.ROOT)
public class RoadController {
    private RoadService roadService;

    @PostMapping(RoadRoutes.POST)
    public RoadDTO createRoad(@RequestBody RoadDTO roadDTO, @RequestParam int capacity){
        return roadService.createRoad(roadDTO, capacity);
    }
}
