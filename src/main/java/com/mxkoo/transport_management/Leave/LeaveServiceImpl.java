package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverRepository;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class LeaveServiceImpl implements LeaveService{
    private final DriverRepository driverRepository;
    private final LeaveRepository leaveRepository;

    public void createLeaveRequest(Long driverId, LocalDate start, LocalDate end) throws Exception{
        if (start.isAfter(end)){
            throw new IllegalArgumentException();
        }


        int leaveDays = (int) ChronoUnit.DAYS.between(start, end) + 1;

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono kierowcy"));

        if (driver.getRoads().stream()
                .anyMatch(road -> road.getDepartureDate().isBefore(end) && road.getArrivalDate().isAfter(start))) {
            throw new Exception("Masz trasę w tym terminie");
        }


        if (driver.getDaysOffLeft() <  leaveDays){
            throw new IllegalStateException("Zbyt malo wolnych dni dostepnych");
        }

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)){
            int driversOnLeave = leaveRepository.countActiveLeavesOnDate(date);
            if (driversOnLeave >= 2) {
                throw new IllegalStateException("Za duzo kierowcow na urlopie w dniu: " + date);
            }

        }

        Leave leave = new Leave();
        leave.setDriver(driver);
        leave.setStart(start);
        leave.setEnd(end);
        leaveRepository.save(leave);

        driver.setDaysOffLeft(driver.getDaysOffLeft() - leaveDays);
        driver.setDriverStatus(DriverStatus.ON_VACATION);
        driverRepository.save(driver);

        scheduleDriverStatusUpdate(driverId, end.plusDays(1));
    }

    public List<LeaveDTO> getAllLeaves(){
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
                .map(LeaveMapper::mapToDTO)
                .toList();
    }
    public LeaveDTO getLeaveById(Long id) throws Exception {
        Leave leave = leaveRepository.findById(id).orElseThrow(Exception::new);
        return LeaveMapper.mapToDTO(leave);
    }

    private void scheduleDriverStatusUpdate(Long driverId, LocalDate updateDate) {
        Runnable task = () -> {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono kierowcy"));
            driver.setDriverStatus(DriverStatus.WAITING_FOR_ROAD);
            driverRepository.save(driver);
        };

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = ChronoUnit.DAYS.between(LocalDate.now(), updateDate) * 24 * 60 * 60;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
    }


}
