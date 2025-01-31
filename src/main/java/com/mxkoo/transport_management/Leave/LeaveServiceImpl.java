package com.mxkoo.transport_management.Leave;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Driver.DriverRepository;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
public class LeaveServiceImpl implements LeaveService{
    private final DriverRepository driverRepository;
    private final LeaveRepository leaveRepository;
    private final LeaveMapper leaveMapper;
    private final DriverMapper driverMapper;
    @Transactional
    public void createLeaveRequest(Long driverId, LocalDate start, LocalDate end) throws Exception{
        if (start.isAfter(end) && start.isBefore(LocalDate.now()) && end.isBefore(LocalDate.now())){
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
        List<Leave> leaves = driver.getLeaves();
        for (Leave leave : leaves){
            if (!(leave.getEnd().isBefore(start) || leave.getStart().isAfter(end))){
                throw new Exception("Masz juz w tym czasie urlop");
            }
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
        scheduleDriverStatusUpdate(driverId, start, DriverStatus.ON_VACATION);
        driver.setDaysOffLeft(driver.getDaysOffLeft() - leaveDays);
        driverRepository.save(driver);

        scheduleDriverStatusUpdate(driverId, end.plusDays(1), DriverStatus.WAITING_FOR_ROAD);
    }
    @Transactional
    public List<LeaveDTO> getAllLeaves(){
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
                .map(leaveMapper::mapToDTO)
                .toList();
    }
    @Transactional
    public LeaveDTO getLeaveById(Long id) throws Exception {
        Leave leave = leaveRepository.findById(id).orElseThrow(Exception::new);
        return leaveMapper.mapToDTO(leave);
    }
    @Transactional
    public LeaveDTO updateLeave(Long leaveId, LeaveDTO toUpdate) throws Exception{
        checkIfExists(leaveId);
        Leave leave = leaveMapper.mapToEntity(getLeaveById(leaveId));
        if (ChronoUnit.DAYS.between(LocalDate.now(), leave.getStart()) < 7){
            throw new IllegalArgumentException("Można edytować urlop do 7 dni przed wyjazdem");
        }
        Driver driver = driverRepository.getById(toUpdate.driverDTO().id());

        if (toUpdate.driverDTO() != null) {
            leave.setDriver(driver);
        }
        if  (toUpdate.start() != null) {
            leave.setStart(toUpdate.start());
        }
        if (toUpdate.end() != null) {
            leave.setEnd(toUpdate.end());
        }
        return leaveMapper.mapToDTO(leaveRepository.save(leave));
    }
    @Transactional
    public void cancelLeave(Long leaveId) throws Exception{
        LeaveDTO leave = getLeaveById(leaveId);
        if ((ChronoUnit.DAYS.between(LocalDate.now(), leave.start())) < 7){
            throw new Exception("Możesz odwołać urlop do 7 dni przed datą jego startu.");
        }
        int leaveDays = (int) ChronoUnit.DAYS.between(leave.start(), leave.end());
        leaveRepository.delete(leaveMapper.mapToEntity(leave));
        Driver driver = driverMapper.mapToEntityWithRoad(leave.driverDTO());
        driver.setDaysOffLeft(driver.getDaysOffLeft() + leaveDays + 1);
        driverRepository.save(driver);
    }

    private void checkIfExists(Long id) {
        if (!leaveRepository.existsById(id)){
            throw new NoSuchElementException("Leave doesn't exist");
        }
    }

    private void scheduleDriverStatusUpdate(Long driverId, LocalDate updateDate, DriverStatus status) {
        Runnable task = () -> {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono kierowcy"));
            driver.setDriverStatus(status);
            driverRepository.save(driver);
        };

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        long delay = ChronoUnit.DAYS.between(LocalDate.now(), updateDate) * 24 * 60 * 60;
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
    }


}
