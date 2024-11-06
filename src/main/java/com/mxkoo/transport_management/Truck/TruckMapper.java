package com.mxkoo.transport_management.Truck;

public class TruckMapper {
    public static Truck mapToEntity(TruckDTO dto){
        return Truck.builder()
                .id(dto.id())
                .licensePlate(dto.licensePlate())
                .capacity(dto.capacity())
                .coordinates(dto.coordinates())
                .inspectionDate(dto.inspectionDate())
                .build();
    }
     public static TruckDTO mapToDTO(Truck truck){
        return TruckDTO.builder()
                .id(truck.getId())
                .licensePlate(truck.getLicensePlate())
                .capacity(truck.getCapacity())
                .coordinates(truck.getCoordinates())
                .inspectionDate(truck.getInspectionDate())
                .build();
    }


}
