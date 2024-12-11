package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Driver.DriverService;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.RoadStatus.RoadStatusService;
import com.mxkoo.transport_management.Truck.*;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
public class RoadServiceImpl implements RoadService {

    private RoadRepository roadRepository;
    private TruckService truckService;
    private DriverService driverService;
    private RoadStatusService roadStatusService;


    public RoadDTO createRoad(RoadDTO roadDTO, int capacity){
        Truck truck = truckService.getAvailableTruck(capacity, roadDTO);
        Driver driver = driverService.getAvailableDriverNotOnRoad(roadDTO);

        if (!truck.getTruckStatus().equals(TruckStatus.WAITING_FOR_ROAD) || !driver.getDriverStatus().equals(DriverStatus.WAITING_FOR_ROAD)){
            throw new IllegalArgumentException("Pojazd lub kierowca nie jest gotowy do drogi");
        }
        validateDate(roadDTO.departureDate(), roadDTO.arrivalDate());
        Road road = new Road();
        road.setFrom(roadDTO.from());
        road.setVia(roadDTO.via());
        road.setTo(roadDTO.to());
        road.setDepartureDate(roadDTO.departureDate());
        road.setArrivalDate(roadDTO.arrivalDate());
        road.setTruck(truck);
        road.setDriver(driver);
        roadStatusService.setStatusForRoad(road);
        return RoadMapper.mapToDTO(roadRepository.save(road));
    }


    public RoadDTO updateRoad(Long id, RoadDTO toUpdate){
        checkIfExists(id);
        Road road = RoadMapper.mapToEntity(getRoadById(id));
        if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) < 7){
            throw new IllegalArgumentException("Można edytować trasę do 7 dni przed wyjazdem");
        }

        if (toUpdate.from() != null) {
            road.setFrom(toUpdate.from());
        }
        if  (toUpdate.via() != null) {
            road.setVia(toUpdate.via());
        }
        if (toUpdate.to() != null) {
            road.setTo(toUpdate.to());
        }
        if(toUpdate.departureDate() != null){
            road.setDepartureDate(toUpdate.departureDate());
        }
        if(toUpdate.arrivalDate() != null){
            road.setArrivalDate(toUpdate.arrivalDate());
        }
        if(toUpdate.truckDTO() != null){
            road.setTruck(TruckMapper.mapToEntityWithRoad(toUpdate.truckDTO()));
        }
        if(toUpdate.driverDTO() != null){
            road.setDriver(DriverMapper.mapToEntityWithRoad(toUpdate.driverDTO()));
        }
        if (toUpdate.roadStatus() != null){
            road.setRoadStatus(toUpdate.roadStatus());
        }
        return RoadMapper.mapToDTO(roadRepository.save(road));
    }

    public List<RoadDTO> getAllRoads(){
        List<Road> roads = roadRepository.findAll();
        return roads.stream()
                .map(RoadMapper::mapToDTO)
                .toList();
    }
    public void deleteAllRoads(){
        var roads = roadRepository.findAll();
        roadRepository.deleteAll(roads);
    }

    public RoadDTO getRoadById(Long id){
        checkIfExists(id);
        return RoadMapper.mapToDTO(roadRepository.findById(id).orElseThrow());
    }

    private void checkIfExists(Long id) {
        if (!roadRepository.existsById(id)){
            throw new NoSuchElementException("Road doesn't exist");
        }
    }

    private void validateDate(LocalDate departureDate, LocalDate arrivalDate){
        if (arrivalDate.isBefore(departureDate)){
            throw new DateTimeException("Data przyjazdu musi być po dacie wyjazdu");
        }
        if (departureDate.isAfter(arrivalDate)){
            throw new DateTimeException("Data wyjazdu musi być przed datą przyjazdu");
        }
    }
}
