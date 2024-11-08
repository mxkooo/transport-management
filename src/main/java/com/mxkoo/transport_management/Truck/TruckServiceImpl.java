package com.mxkoo.transport_management.Truck;


import com.mxkoo.transport_management.Coordinates.Coordinates;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Truck getTruck(Long id) throws Exception{
        return truckRepository.findById(id).orElseThrow(Exception::new);
    }
    public TruckDTO updateTruck(Long id, TruckDTO toUpdate) throws Exception {
        checkIfExists(id);
        Truck truck = getTruck(id);
        if (toUpdate.licensePlate() != null) {
            truck.setLicensePlate(toUpdate.licensePlate());
        }
        if  (toUpdate.capacity() != null) {
            truck.setCapacity(toUpdate.capacity());
        }
        if (toUpdate.inspectionDate() != null) {
            truck.setInspectionDate(toUpdate.inspectionDate());
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
        Truck truck = getTruck(truckId);
        truck.setCoordinates(new Coordinates(coordinates.getX(), coordinates.getY()));
        TruckMapper.mapToDTO(truckRepository.save(truck));

    }

}
