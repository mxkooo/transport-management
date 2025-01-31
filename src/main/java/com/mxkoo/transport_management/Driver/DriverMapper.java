package com.mxkoo.transport_management.Driver;
import com.mxkoo.transport_management.Road.RoadMapper;
import com.mxkoo.transport_management.Truck.TruckMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {RoadMapper.class, TruckMapper.class})
public interface DriverMapper {

    public Driver mapToEntityWithRoad(DriverDTO driverDTO);
    public DriverDTO mapToDTOWithRoad(Driver driver);


}
