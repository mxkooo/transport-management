package com.mxkoo.transport_management.Leave;

public class LeaveMapper {

    public static Leave mapToEntity(LeaveDTO leaveDTO){
        return Leave.builder()
                .id(leaveDTO.id())
                .driver(leaveDTO.driver())
                .start(leaveDTO.start())
                .end(leaveDTO.end())
                .build();
    }

    public static LeaveDTO mapToDTO(Leave leave){
        return LeaveDTO.builder()
                .id(leave.getId())
                .driver(leave.getDriver())
                .start(leave.getStart())
                .end(leave.getEnd())
                .build();
    }
}
