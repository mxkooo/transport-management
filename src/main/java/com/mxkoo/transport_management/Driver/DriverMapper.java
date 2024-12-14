package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadDTO;
import com.mxkoo.transport_management.Truck.Truck;
import com.mxkoo.transport_management.Truck.TruckMapper;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class DriverMapper {

    public static Driver mapToEntityWithRoad(DriverDTO driverDTO){

        Driver driver = Driver.builder()
                .id(driverDTO.id())
                .name(driverDTO.name())
                .lastName(driverDTO.lastName())
                .coordinates(driverDTO.coordinates())
                .email(driverDTO.email())
                .contactNumber(driverDTO.contactNumber())
                .driverStatus(driverDTO.driverStatus())
                .daysOffLeft(driverDTO.daysOffLeft())
                .leaves(driverDTO.leaves())
                .build();

        List<Road> roads = Optional.ofNullable(driverDTO.roads())
                .orElse(Collections.emptyList())
                .stream()
                .map(dto -> {
                    Truck truck = TruckMapper.mapToEntityWithRoad(dto.truckDTO());
                    return Road.builder()
                            .id(dto.id())
                            .from(dto.from())
                            .via(dto.via())
                            .to(dto.to())
                            .departureDate(dto.departureDate())
                            .arrivalDate(dto.arrivalDate())
                            .truck(truck)
                            .driver(driver)
                            .roadStatus(dto.roadStatus())
                            .build();
                }).filter(Objects::nonNull)
                .collect(Collectors.toList());
        driver.setRoads(roads);

        return driver;
    }

    public static DriverDTO mapToDTOWithRoad(Driver driver){

        List<RoadDTO> roadDTOs = Optional.ofNullable(driver.getRoads())
                .orElse(Collections.emptyList())
                .stream()
                .map(road -> RoadDTO.builder()
                        .id(road.getId())
                        .to(road.getTo())
                        .via(road.getVia())
                        .from(road.getFrom())
                        .departureDate(road.getDepartureDate())
                        .arrivalDate(road.getArrivalDate())
                        .truckDTO(TruckMapper.mapToDTO(road.getTruck()))
                        .driverDTO(DriverMapper.mapToDTO(road.getDriver()))
                        .roadStatus(road.getRoadStatus())
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
                .roads(roadDTOs)
                .driverStatus(driver.getDriverStatus())
                .daysOffLeft(driver.getDaysOffLeft())
                .leaves(driver.getLeaves())
                .build();
    }

    public static Driver mapToEntity(DriverDTO driverDTO){
        return Driver.builder()
                .id(driverDTO.id())
                .name(driverDTO.name())
                .lastName(driverDTO.lastName())
                .coordinates(driverDTO.coordinates())
                .email(driverDTO.email())
                .contactNumber(driverDTO.contactNumber())
                .driverStatus(driverDTO.driverStatus())
                .daysOffLeft(driverDTO.daysOffLeft())
                .leaves(driverDTO.leaves())
                .build();
    }
    public static DriverDTO mapToDTO(Driver driver){
        return DriverDTO.builder()
                .id(driver.getId())
                .name(driver.getName())
                .lastName(driver.getLastName())
                .coordinates(driver.getCoordinates())
                .email(driver.getEmail())
                .contactNumber(driver.getContactNumber())
                .driverStatus(driver.getDriverStatus())
                .daysOffLeft(driver.getDaysOffLeft())
                .leaves(driver.getLeaves())
                .build();
    }


}
