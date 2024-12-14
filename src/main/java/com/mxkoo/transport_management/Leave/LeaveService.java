package com.mxkoo.transport_management.Leave;

import java.time.LocalDate;
import java.util.List;

public interface LeaveService {
    void createLeaveRequest(Long driverId, LocalDate start, LocalDate end) throws Exception;
    List<LeaveDTO> getAllLeaves();
    LeaveDTO getLeaveById(Long id) throws Exception;
}
