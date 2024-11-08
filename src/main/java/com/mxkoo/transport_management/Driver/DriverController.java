package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DriverRoutes.ROOT)
@AllArgsConstructor
public class DriverController {


    private final DriverService driverService;

    @PostMapping(DriverRoutes.POST)
    public DriverDTO createDriver(@RequestBody @Validated DriverDTO driverDTO){
        return driverService.createDriver(driverDTO);
    }

    @GetMapping(DriverRoutes.GET + "/{id}")
    public DriverDTO getDriverById(@PathVariable Long id) throws Exception{
        return driverService.getDriverById(id);
    }
    @GetMapping(DriverRoutes.GET + "/all")
    public List<DriverDTO> getAllDrivers(){
        return driverService.getAllDrivers();
    }

    @DeleteMapping(DriverRoutes.DELETE + "/{id}")
    public void deleteDriver(@PathVariable Long id) throws Exception{
        driverService.deleteById(id);
    }

    @PatchMapping(DriverRoutes.UPDATE + "/{id}")
    public DriverDTO updateDriver(@PathVariable Long id, @RequestBody DriverDTO toUpdate) throws Exception{
        return driverService.updateDriver(id, toUpdate);
    }

    @PostMapping("/coordinates" + "/{driverId}")
    public void setCoordinatesForDriver(@PathVariable Long driverId, @RequestBody Coordinates coordinates) throws Exception {
        driverService.setCoordinatesForDriver(driverId, coordinates);
    }


}
