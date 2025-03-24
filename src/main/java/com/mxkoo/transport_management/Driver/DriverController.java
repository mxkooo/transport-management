package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DriverRoutes.ROOT)
@AllArgsConstructor
public class DriverController {


    private final DriverService driverService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping(DriverRoutes.POST)
    public DriverDTO createDriver(@RequestBody @Validated DriverDTO driverDTO){
        messagingTemplate.convertAndSend("/topic/drivers", driverService.getAllDrivers());
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
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDriver(@PathVariable Long id) throws Exception{
        messagingTemplate.convertAndSend("/topic/drivers", driverService.getAllDrivers());
        driverService.deleteById(id);
    }
    @DeleteMapping(DriverRoutes.DELETE + "/all")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteAllDrivers(){
        driverService.deleteAllDrivers();
    }

    @PatchMapping(DriverRoutes.UPDATE + "/{id}")
    public DriverDTO updateDriver(@PathVariable Long id, @RequestBody DriverDTO toUpdate) throws Exception{
        messagingTemplate.convertAndSend("/topic/drivers", driverService.getAllDrivers());
        return driverService.updateDriver(id, toUpdate);
    }

    @PatchMapping("/coordinates" + "/{driverId}")
    public DriverDTO setCoordinatesForDriver(@PathVariable Long driverId, @RequestBody Coordinates coordinates) throws Exception {
        return driverService.setCoordinatesForDriver(driverId, coordinates);
    }


}
