package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatusService;
import com.mxkoo.transport_management.Road.RoadDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService{

    private DriverRepository repository;
    private DriverStatusService driverStatusService;
    private DriverMapper driverMapper;

    @Transactional
    public DriverDTO createDriver(DriverDTO driverDTO){
        Driver driver = new Driver();
        driver.setName(driverDTO.name());
        driver.setLastName(driverDTO.lastName());
        driver.setEmail(driverDTO.email());
        driver.setContactNumber(driverDTO.contactNumber());
        driverStatusService.setStatusForDriver(driver);
        return driverMapper.mapToDTOWithRoad(repository.save(driver));
    }
    @Transactional
    public DriverDTO getDriverById(Long id) throws Exception {
        Driver driver = repository.findById(id).orElseThrow(Exception::new);
        return driverMapper.mapToDTOWithRoad(driver);
    }
    @Transactional
    public List<DriverDTO> getAllDrivers(){
        List<Driver> drivers = repository.findAll();
        return drivers.stream()
                .map(driverMapper::mapToDTOWithRoad)
                .toList();
    }
    @Transactional
    public void deleteById(Long id) throws Exception{
        checkIfExists(id);
        repository.deleteById(id);
    }
    @Transactional
    public void deleteAllDrivers(){
        var drivers = repository.findAll();
        repository.deleteAll(drivers);
    }
    @Transactional
    public DriverDTO findDriver(Long id) throws Exception{
        return driverMapper.mapToDTOWithRoad(repository.findById(id).orElseThrow(Exception::new));
    }
    @Transactional
    public DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception {
        checkIfExists(id);
        Driver driver = driverMapper.mapToEntityWithRoad(findDriver(id));
        if (toUpdate.name() != null) {
            driver.setName(toUpdate.name());
        }
        if  (toUpdate.lastName() != null) {
            driver.setLastName(toUpdate.lastName());
        }
        if (toUpdate.email() != null) {
            driver.setEmail(toUpdate.email());
        }
        if (toUpdate.contactNumber() != null) {
            driver.setContactNumber(toUpdate.contactNumber());
        }
        if (toUpdate.driverStatus() != null){
            driver.setDriverStatus(toUpdate.driverStatus());
        }
        return driverMapper.mapToDTOWithRoad(repository.save(driver));
    }

    @Transactional
    public DriverDTO setCoordinatesForDriver(Long driverId, Coordinates coordinates) throws Exception {
        Driver driver = repository.findById(driverId)
                .orElseThrow(() -> new Exception("Driver not found with ID: " + driverId));
        driver.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        return driverMapper.mapToDTOWithRoad(driver);
    }

    @Transactional
    public Driver getAvailableDriverNotOnRoad(RoadDTO road) {
        return repository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD)
                .stream()
                .filter(driver -> driver.getRoads().stream().noneMatch(eachRoad ->
                        (eachRoad.getArrivalDate().isBefore(road.arrivalDate()) && eachRoad.getDepartureDate().isAfter(road.arrivalDate())) ||
                                (eachRoad.getArrivalDate().isBefore(road.departureDate()) && eachRoad.getDepartureDate().isAfter(road.departureDate())) ||
                                (eachRoad.getArrivalDate().equals(road.arrivalDate()) || eachRoad.getDepartureDate().equals(road.departureDate()))
                ))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kierowcy"));
    }


    private void checkIfExists(Long id) throws Exception {
        if (!repository.existsById(id)){
            throw new Exception("Driver doesn't exist");
        }
    }

}
