package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverDTO;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Truck.Truck;
import com.mxkoo.transport_management.Truck.TruckDTO;
import com.mxkoo.transport_management.Truck.TruckMapper;

import java.util.Optional;

public class RoadMapper {

    public  static Road mapToEntity(RoadDTO roadDTO){
        Driver driver = Optional.ofNullable(roadDTO.driverDTO())
                .map(DriverMapper::mapToEntityWithRoad)
                .orElse(null);

        Truck truck = Optional.ofNullable(roadDTO.truckDTO())
                .map(TruckMapper::mapToEntityWithRoad)
                .orElse(null);

        return Road.builder()
                .id(roadDTO.id())
                .from(roadDTO.from())
                .via(roadDTO.via())
                .to(roadDTO.to())
                .departureDate(roadDTO.departureDate())
                .arrivalDate(roadDTO.arrivalDate())
                .distance(roadDTO.distance())
                .price(roadDTO.price())
                .truck(truck)
                .driver(driver)
                .roadStatus(roadDTO.roadStatus())
                .build();


    }

    public static RoadDTO mapToDTO(Road road){
        DriverDTO driverDTO = Optional.ofNullable(road.getDriver())
                .map(DriverMapper::mapToDTOWithRoad)
                .orElse(null);

        TruckDTO truckDTO = Optional.ofNullable(road.getTruck())
                .map(TruckMapper::mapToDTOWithRoad)
                .orElse(null);

        return RoadDTO.builder()
                .id(road.getId())
                .from(road.getFrom())
                .via(road.getVia())
                .to(road.getTo())
                .departureDate(road.getDepartureDate())
                .arrivalDate(road.getArrivalDate())
                .distance(road.getDistance())
                .price(road.getPrice())
                .truckDTO(truckDTO)
                .driverDTO(driverDTO)
                .roadStatus(road.getRoadStatus())
                .build();


    }
}
