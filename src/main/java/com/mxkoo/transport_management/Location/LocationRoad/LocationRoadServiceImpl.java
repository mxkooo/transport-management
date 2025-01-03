package com.mxkoo.transport_management.Location.LocationRoad;

import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationRoadServiceImpl implements LocationRoadService{
    private final RoadRepository roadRepository;

    public List<LocationRoadDTO> getRoadLocation() {
        return roadRepository.findAll()
                .stream()
                .map(road -> new LocationRoadDTO(
                        road.getId(),
                        road.getFrom(),
                        road.getVia(),
                        road.getTo(),
                        road.getDepartureDate(),
                        road.getArrivalDate(),
                        road.getDistance(),
                        road.getPrice(),
                        road.getDriver().getId(),
                        road.getTruck().getId()
                ))
                .collect(Collectors.toList());
    }

    public void updateRoadDistance(Long id, Double distance, Double price) {
        Road road = roadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Road not found"));
        road.setDistance(distance);
        road.setPrice(price);
        roadRepository.save(road);
    }
}
