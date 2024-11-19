package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverService;
import com.mxkoo.transport_management.RoadStatus.RoadStatusService;
import com.mxkoo.transport_management.Truck.Truck;
import com.mxkoo.transport_management.Truck.TruckService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.LocalDate;
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
        Truck truck = truckService.getAvailableTruck(capacity);
        Driver driver = driverService.getAvailableDriver();

        if (!truck.getTruckStatus().equals("WAITING_FOR_ROAD") || !driver.getDriverStatus().equals("WAITING_FOR_ROAD")){
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
        roadStatusService.setStatusForRoad(roadDTO.departureDate(), roadDTO.arrivalDate(), road);
        return RoadMapper.mapToDTO(roadRepository.save(road));
    }

    private void validateDate(LocalDate departureDate, LocalDate arrivalDate){
        if (arrivalDate.isBefore(departureDate)){
            throw new DateTimeException("Data przyjazdu musi być po dacie wyjazdu");
        }
        if (departureDate.isAfter(arrivalDate)){
            throw new DateTimeException("Data wyjazdu musi być przed datą przyjazdu");
        }
    }


    public List<RoadDTO> getAllRoads(){
        List<Road> roads = roadRepository.findAll();
        return roads.stream()
                .map(RoadMapper::mapToDTO)
                .toList();
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

}
