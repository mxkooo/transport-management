package com.mxkoo.transport_management.Truck;


import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(TruckRoutes.ROOT)
@AllArgsConstructor
public class TruckController {
    private TruckService truckService;

    @PostMapping(TruckRoutes.POST)
    public TruckDTO createTruck(@RequestBody @Validated TruckDTO truckDTO){
        return truckService.createTruck(truckDTO);
    }

    @GetMapping(TruckRoutes.GET + "/{id}")
    public TruckDTO getTruck(@PathVariable Long id) throws Exception{
        return truckService.getTruckById(id);
    }

    @GetMapping(TruckRoutes.GET + "/all")
    public List<TruckDTO> getAllTrucks(){
        return truckService.getAllTrucks();
    }

    @DeleteMapping(TruckRoutes.DELETE + "/{id}")
    public void deleteTruck(@PathVariable Long id) throws Exception{
        truckService.deleteById(id);
    }

    @PatchMapping(TruckRoutes.UPDATE + "/{id}")
    public TruckDTO updateTruck(@PathVariable Long id, @RequestBody TruckDTO truckDTO) throws Exception {
        return truckService.updateTruck(id,truckDTO);
    }

}
