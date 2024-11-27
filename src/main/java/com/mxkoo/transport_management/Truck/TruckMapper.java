package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadDTO;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TruckMapper {
    public static Truck mapToEntityWithRoad(TruckDTO truckDTO){

        Truck truck = Truck.builder()
                .id(truckDTO.id())
                .licensePlate(truckDTO.licensePlate())
                .capacity(truckDTO.capacity())
                .coordinates(truckDTO.coordinates())
                .inspectionDate(truckDTO.inspectionDate())
                .truckStatus(truckDTO.truckStatus())
                .build();


        List<Road> roads = Optional.ofNullable(truckDTO.roads())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> {
                    Driver driver = DriverMapper.mapToEntity(dto.driverDTO());
                    return Road.builder()
                            .id(dto.id())
                            .to(dto.to())
                            .via(dto.via())
                            .from(dto.from())
                            .departureDate(dto.departureDate())
                            .arrivalDate(dto.arrivalDate())
                            .truck(truck)
                            .driver(driver)
                            .roadStatus(dto.roadStatus())
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        truck.setRoads(roads);

        return truck;
    }


    public static TruckDTO mapToDTOWithRoad(Truck truck) {
        List<RoadDTO> roadDTOs = Optional.ofNullable(truck.getRoads())
                .orElse(Collections.emptyList())
                .stream()
                .map(road -> RoadDTO.builder()
                        .id(road.getId())
                        .to(road.getTo())
                        .via(road.getVia())
                        .from(road.getFrom())
                        .departureDate(road.getDepartureDate())
                        .arrivalDate(road.getArrivalDate())
                        .driverDTO(DriverMapper.mapToDTO(road.getDriver()))
                        .truckDTO(mapToDTO(road.getTruck()))
                        .roadStatus(road.getRoadStatus())
                        .build())
                .collect(Collectors.toList());

        return TruckDTO.builder()
                .id(truck.getId())
                .licensePlate(truck.getLicensePlate())
                .capacity(truck.getCapacity())
                .coordinates(truck.getCoordinates())
                .inspectionDate(truck.getInspectionDate())
                .roads(roadDTOs)
                .truckStatus(truck.getTruckStatus())
                .build();

    }

    public static Truck mapToEntity(TruckDTO truckDTO){
        return Truck.builder()
                .id(truckDTO.id())
                .licensePlate(truckDTO.licensePlate())
                .capacity(truckDTO.capacity())
                .coordinates(truckDTO.coordinates())
                .inspectionDate(truckDTO.inspectionDate())
                .truckStatus(truckDTO.truckStatus())
                .build();
    }

    public static TruckDTO mapToDTO(Truck truck){
        return TruckDTO.builder()
                .id(truck.getId())
                .licensePlate(truck.getLicensePlate())
                .capacity(truck.getCapacity())
                .coordinates(truck.getCoordinates())
                .inspectionDate(truck.getInspectionDate())
                .truckStatus(truck.getTruckStatus())
                .build();
    }



}
