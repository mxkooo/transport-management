package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.DriverMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DriverMapper.class})
public interface LeaveMapper {
    public Leave mapToEntity(LeaveDTO leaveDTO);
    public LeaveDTO mapToDTO(Leave leave);


}
