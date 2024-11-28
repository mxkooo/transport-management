package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.Road;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService{

    private DriverRepository repository;

    public DriverDTO createDriver(DriverDTO driverDTO){
        Driver driver = new Driver();
        driver.setName(driverDTO.name());
        driver.setLastName(driverDTO.lastName());
        driver.setEmail(driverDTO.email());
        driver.setContactNumber(driverDTO.contactNumber());
        return DriverMapper.mapToDTOWithRoad(repository.save(driver));
    }

    public DriverDTO getDriverById(Long id) throws Exception {
        Driver driver = repository.findById(id).orElseThrow(Exception::new);
        return DriverMapper.mapToDTOWithRoad(driver);
    }

    public List<DriverDTO> getAllDrivers(){
        List<Driver> drivers = repository.findAll();
        return drivers.stream()
                .map(DriverMapper::mapToDTOWithRoad)
                .toList();
    }

    public void deleteById(Long id) throws Exception{
        checkIfExists(id);
        repository.deleteById(id);
    }
    public void deleteAllDrivers(){
        var drivers = repository.findAll();
        repository.deleteAll(drivers);
    }

    public DriverDTO findDriver(Long id) throws Exception{
        return DriverMapper.mapToDTOWithRoad(repository.findById(id).orElseThrow(Exception::new));
    }

    public DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception {
        checkIfExists(id);
        Driver driver = DriverMapper.mapToEntityWithRoad(findDriver(id));
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
        return DriverMapper.mapToDTOWithRoad(repository.save(driver));
    }

    public DriverDTO setCoordinatesForDriver(Long driverId, Coordinates coordinates) throws Exception{
        checkIfExists(driverId);
        Driver driver = DriverMapper.mapToEntityWithRoad(getDriverById(driverId));
        driver.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        return DriverMapper.mapToDTOWithRoad(repository.save(driver));

    }

    public Driver getAvailableDriverNotOnRoad(Road road){
        Driver driver = repository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kierwocy"));
        List<Road> roads = driver.getRoads();
        for (Road eachRoad : roads){
            if (eachRoad.getArrivalDate().equals(road.getArrivalDate()) &&
                eachRoad.getDepartureDate().equals(road.getDepartureDate()) ||
            eachRoad.getArrivalDate().isAfter(road.getArrivalDate()) ||
                    eachRoad.getDepartureDate().isBefore(road.getDepartureDate()))
            {
                throw new NoSuchElementException("Nie znaleziono kierowcy");
            }

        }

        return driver;
    }

    public Driver getAvailableDriver(){
         return repository.findDriverByDriverStatus(DriverStatus.WAITING_FOR_ROAD)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kierowcy"));

    }
    private void checkIfExists(Long id) throws Exception {
        if (!repository.existsById(id)){
            throw new Exception("Driver doesn't exist");
        }
    }

}
