package com.mxkoo.transport_management.Driver;

public class DriverMapper {

    public static Driver mapToEntity(DriverDTO dto){
        return Driver.builder()
                .id(dto.id())
                .name(dto.name())
                .lastName(dto.lastName())
                .coordinates(dto.coordinates())
                .email(dto.email())
                .contactNumber(dto.contactNumber())
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
                .build();
    }


}
