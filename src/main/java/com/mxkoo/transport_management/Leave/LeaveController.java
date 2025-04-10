package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.DriverRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(DriverRoutes.ROOT)
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/leave/{driverId}")
    public void createLeaveRequest(@PathVariable Long driverId, @RequestParam LocalDate start, @RequestParam LocalDate end) throws Exception {
        leaveService.createLeaveRequest(driverId, start, end);
    }

    @GetMapping("/leave/all")
    public List<LeaveDTO> getAllLeaves() {
        return leaveService.getAllLeaves();
    }

    @GetMapping("/leave/{id}")
    public LeaveDTO getLeaveById(@PathVariable Long id) throws Exception {
        return leaveService.getLeaveById(id);
    }

    @DeleteMapping("/leave/cancel/{leaveId}")
    public void cancelLeave(@PathVariable Long leaveId) throws Exception {
        leaveService.cancelLeave(leaveId);
    }


}
