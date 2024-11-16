package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TruckMapper {
    public static Truck mapToEntity(TruckDTO truckDTO){

        List<Road> roads = Optional.ofNullable(truckDTO.roads())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> Road.builder()
                        .id(dto.id())
                        .to(dto.to())
                        .via(dto.via())
                        .from(dto.from())
                        .departureDate(dto.departureDate())
                        .arrivalDate(dto.arrivalDate())
                        .truck(null)
                        .driver(null)
                        .roadStatus(dto.roadStatus())
                        .build()
                        ).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Truck.builder()
                .id(truckDTO.id())
                .licensePlate(truckDTO.licensePlate())
                .capacity(truckDTO.capacity())
                .coordinates(truckDTO.coordinates())
                .inspectionDate(truckDTO.inspectionDate())
                .roads(roads)
                .truckStatus(truckDTO.truckStatus())
                .build();
    }

    public static TruckDTO mapToDTO(Truck truck) {
        List<RoadDTO> roads = Optional.ofNullable(truck.getRoads())
                .orElse(Collections.emptyList())
                .stream()
                .map(road -> RoadDTO.builder()
                        .id(road.getId())
                        .to(road.getTo())
                        .via(road.getVia())
                        .from(road.getFrom())
                        .departureDate(road.getDepartureDate())
                        .arrivalDate(road.getArrivalDate())
                        .truckDTO(null)
                        .driverDTO(null)
                        .roadStatus(road.getRoadStatus())
                        .build()).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return TruckDTO.builder()
                .id(truck.getId())
                .licensePlate(truck.getLicensePlate())
                .capacity(truck.getCapacity())
                .coordinates(truck.getCoordinates())
                .inspectionDate(truck.getInspectionDate())
                .roads(roads)
                .truckStatus(truck.getTruckStatus())
                .build();
    }



}
