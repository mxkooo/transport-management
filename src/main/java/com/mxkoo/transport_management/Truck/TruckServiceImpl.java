package com.mxkoo.transport_management.Truck;


import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.RoadDTO;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TruckServiceImpl implements TruckService {
    private TruckRepository truckRepository;
    private TruckStatusService truckStatusService;
    private TruckMapper truckMapper;

    @Transactional
    public TruckDTO createTruck(TruckDTO truckDTO){
        Truck truck = new Truck();
        truck.setLicensePlate(truckDTO.licensePlate());
        truck.setCapacity(truckDTO.capacity());
        truck.setInspectionDate(truckDTO.inspectionDate());
        truckStatusService.setStatusForTruck(truck);
        return truckMapper.mapToDTOWithRoad(truckRepository.save(truck));
    }
    @Transactional
    public TruckDTO getTruckById(Long id) throws Exception {
        Truck truck = truckRepository.findById(id).orElseThrow(Exception::new);
        return truckMapper.mapToDTOWithRoad(truck);
    }
    @Transactional
    public List<TruckDTO> getAllTrucks(){
        List<Truck> trucks = truckRepository.findAll();
        return trucks.stream()
                .map(truckMapper::mapToDTOWithRoad)
                .toList();
    }
    @Transactional
    public void deleteById(Long id) throws Exception{
        checkIfExists(id);
        truckRepository.deleteById(id);
    }
    @Transactional
    public void deleteAllTrucks(){
        var trucks = truckRepository.findAll();
        truckRepository.deleteAll(trucks);
    }
    @Transactional
    public TruckDTO getTruck(Long id) throws Exception{
        return truckMapper.mapToDTOWithRoad(truckRepository.findById(id).orElseThrow(Exception::new));
    }
    @Transactional
    public TruckDTO updateTruck(Long id, TruckDTO toUpdate) throws Exception {
        checkIfExists(id);
        Truck truck = truckMapper.mapToEntityWithRoad(getTruck(id));
        if (toUpdate.licensePlate() != null) {
            truck.setLicensePlate(toUpdate.licensePlate());
        }
        if  (toUpdate.capacity() != null) {
            truck.setCapacity(toUpdate.capacity());
        }
        if (toUpdate.inspectionDate() != null) {
            truck.setInspectionDate(toUpdate.inspectionDate());
        }
        if(toUpdate.truckStatus() != null){
            truck.setTruckStatus(toUpdate.truckStatus());
        }
        return truckMapper.mapToDTOWithRoad(truckRepository.save(truck));
    }


    @Transactional
    public TruckDTO setCoordinatesForTruck(Long truckId, Coordinates coordinates) throws Exception {
        Truck truck = truckRepository.findById(truckId)
                .orElseThrow(() -> new Exception("Truck not found with ID: " + truckId));
        truck.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        return truckMapper.mapToDTOWithRoad(truck);
    }

    @Transactional
    public Truck getAvailableTruck(int capacity, RoadDTO road) {
        return truckRepository.findByCapacityAndTruckStatus(capacity, TruckStatus.WAITING_FOR_ROAD)
                .stream()
                .filter(truck -> truck.getRoads().stream().noneMatch(eachRoad ->
                        (eachRoad.getArrivalDate().isBefore(road.arrivalDate()) && eachRoad.getDepartureDate().isAfter(road.arrivalDate())) ||
                                (eachRoad.getArrivalDate().isBefore(road.departureDate()) && eachRoad.getDepartureDate().isAfter(road.departureDate())) ||
                                (eachRoad.getArrivalDate().equals(road.arrivalDate()) || eachRoad.getDepartureDate().equals(road.departureDate()))
                ))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono pojazdu"));
    }

    private void checkIfExists(Long id) throws Exception{
        if (!truckRepository.existsById(id)){
            throw new Exception("Truck doesn't exist");
        }
    }

}
