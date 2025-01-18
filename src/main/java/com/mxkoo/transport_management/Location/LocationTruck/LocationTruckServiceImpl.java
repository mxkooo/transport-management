package com.mxkoo.transport_management.Location.LocationTruck;

import com.mxkoo.transport_management.Truck.TruckRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationTruckServiceImpl implements LocationTruckService{
    private final TruckRepository truckRepository;
    public List<LocationTruckDTO> getTruckLocations() {
        return truckRepository.findAll()
                .stream()
                .map(truck -> new LocationTruckDTO(
                        truck.getId(),
                        truck.getCoordinates(),
                        truck.getLicensePlate(),
                        truck.getTruckStatus()
                ))
                .collect(Collectors.toList());
    }
}
