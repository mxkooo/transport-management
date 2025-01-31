package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Truck.TruckMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {DriverMapper.class, TruckMapper.class})
public interface RoadMapper {

    public Road mapToEntity(RoadDTO roadDTO);

    public RoadDTO mapToDTO(Road road);
}
