package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
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
        return DriverMapper.mapToDTO(repository.save(driver));
    }

    public DriverDTO getDriverById(Long id) throws Exception {
        Driver driver = repository.findById(id).orElseThrow(Exception::new);
        return DriverMapper.mapToDTO(driver);
    }

    public List<DriverDTO> getAllDrivers(){
        List<Driver> drivers = repository.findAll();
        return drivers.stream()
                .map(DriverMapper::mapToDTO)
                .toList();
    }

    public void deleteById(Long id) throws Exception{
        checkIfExists(id);
        repository.deleteById(id);
    }

    public DriverDTO findDriver(Long id) throws Exception{
        return DriverMapper.mapToDTO(repository.findById(id).orElseThrow(Exception::new));
    }

    public DriverDTO updateDriver(Long id, DriverDTO toUpdate) throws Exception {
        checkIfExists(id);
        Driver driver = DriverMapper.mapToEntity(findDriver(id));
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
        return DriverMapper.mapToDTO(repository.save(driver));
    }

    private void checkIfExists(Long id) throws Exception {
        if (!repository.existsById(id)){
            throw new Exception("Driver doesn't exist");
        }
    }

    public void setCoordinatesForDriver(Long driverId, Coordinates coordinates) throws Exception{
        checkIfExists(driverId);
        Driver driver = DriverMapper.mapToEntity(getDriverById(driverId));
        driver.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        DriverMapper.mapToDTO(repository.save(driver));

    }

    public Driver getAvailableDriver(){
         return repository.findAll()
                .stream()
                .filter(this::isAvailable)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono kierowcy"));

    }
    public boolean isAvailable(Driver driver){
        return driver.getDriverStatus()
                .equals(DriverStatus.WAITING_FOR_ROAD);
    }


}
