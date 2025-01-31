package com.mxkoo.transport_management.Truck;
import com.mxkoo.transport_management.Driver.DriverMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface TruckMapper {
    public Truck mapToEntityWithRoad(TruckDTO truckDTO);

    public TruckDTO mapToDTOWithRoad(Truck truck);

}
