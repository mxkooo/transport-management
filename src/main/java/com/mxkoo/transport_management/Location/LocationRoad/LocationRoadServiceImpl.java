package com.mxkoo.transport_management.Location.LocationRoad;

import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationRoadServiceImpl implements LocationRoadService {

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
                                     road.getDriver()
                                         .getId(),
                                     road.getTruck()
                                         .getId(),
                                     road.getRoadStatus()
                             ))
                             .collect(Collectors.toList());
    }

}
