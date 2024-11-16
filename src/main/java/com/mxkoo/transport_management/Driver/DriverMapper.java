package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadDTO;
import com.mxkoo.transport_management.Truck.TruckMapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverMapper {

    public static Driver mapToEntity(DriverDTO driverDTO){

        List<Road> roads = Optional.ofNullable(driverDTO.roads())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> Road.builder()
                        .id(dto.id())
                        .from(dto.from())
                        .via(dto.via())
                        .to(dto.to())
                        .departureDate(dto.departureDate())
                        .arrivalDate(dto.arrivalDate())
                        .truck(null)
                        .driver(null)
                        .roadStatus(dto.roadStatus())
                        .build()
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Driver.builder()
                .id(driverDTO.id())
                .name(driverDTO.name())
                .lastName(driverDTO.lastName())
                .coordinates(driverDTO.coordinates())
                .email(driverDTO.email())
                .contactNumber(driverDTO.contactNumber())
                .roads(roads)
                .driverStatus(driverDTO.driverStatus())
                .build();
    }

    public static DriverDTO mapToDTO(Driver driver){

        List<RoadDTO> roads = Optional.ofNullable(driver.getRoads())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> RoadDTO.builder()
                        .id(dto.getId())
                        .from(dto.getFrom())
                        .via(dto.getVia())
                        .to(dto.getTo())
                        .departureDate(dto.getDepartureDate())
                        .arrivalDate(dto.getArrivalDate())
                        .truckDTO(null)
                        .driverDTO(null)
                        .roadStatus(dto.getRoadStatus())
                        .build()
                ).filter(Objects::nonNull)
                .collect(Collectors.toList());

        return DriverDTO.builder()
                .id(driver.getId())
                .name(driver.getName())
                .lastName(driver.getLastName())
                .coordinates(driver.getCoordinates())
                .email(driver.getEmail())
                .contactNumber(driver.getContactNumber())
                .roads(roads)
                .driverStatus(driver.getDriverStatus())
                .build();
    }


}
