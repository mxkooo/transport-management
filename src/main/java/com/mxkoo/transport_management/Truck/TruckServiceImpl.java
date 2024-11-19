package com.mxkoo.transport_management.Truck;


import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class TruckServiceImpl implements TruckService {
    private TruckRepository truckRepository;

    public TruckDTO createTruck(TruckDTO truckDTO){
        Truck truck = new Truck();
        truck.setLicensePlate(truckDTO.licensePlate());
        truck.setCapacity(truckDTO.capacity());
        truck.setInspectionDate(truckDTO.inspectionDate());
        return TruckMapper.mapToDTO(truckRepository.save(truck));
    }

    public TruckDTO getTruckById(Long id) throws Exception {
        Truck truck = truckRepository.findById(id).orElseThrow(Exception::new);
        return TruckMapper.mapToDTO(truck);
    }

    public List<TruckDTO> getAllTrucks(){
        List<Truck> trucks = truckRepository.findAll();
        return trucks.stream()
                .map(TruckMapper::mapToDTO)
                .toList();
    }

    public void deleteById(Long id) throws Exception{
        checkIfExists(id);
        truckRepository.deleteById(id);
    }

    public TruckDTO getTruck(Long id) throws Exception{
        return TruckMapper.mapToDTO(truckRepository.findById(id).orElseThrow(Exception::new));
    }
    public TruckDTO updateTruck(Long id, TruckDTO toUpdate) throws Exception {
        checkIfExists(id);
        Truck truck = TruckMapper.mapToEntity(getTruck(id));
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
        return TruckMapper.mapToDTO(truckRepository.save(truck));
    }

    private void checkIfExists(Long id) throws Exception{
        if (!truckRepository.existsById(id)){
            throw new Exception("Truck doesn't exist");
        }
    }

    public void setCoordinatesForTruck(Long truckId, Coordinates coordinates) throws Exception{
        checkIfExists(truckId);
        Truck truck = TruckMapper.mapToEntity(getTruck(truckId));
        truck.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        TruckMapper.mapToDTO(truckRepository.save(truck));

    }

    public Truck getAvailableTruck(int capacity){

          return truckRepository.findTrucksByCapacity(capacity)
                .stream()
                .filter(this::isAvailable)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Nie znaleziono pojazdu spełniającego dane wymagania"));

    }

    private boolean isAvailable(Truck truck){
        return truck.getTruckStatus()
                .equals(TruckStatus.WAITING_FOR_ROAD);
    }

}
