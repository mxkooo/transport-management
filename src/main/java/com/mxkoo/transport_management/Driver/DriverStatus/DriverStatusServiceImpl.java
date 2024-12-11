package com.mxkoo.transport_management.Driver.DriverStatus;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverRepository;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverStatusServiceImpl implements DriverStatusService {
    private final DriverRepository driverRepository;
    private final RoadRepository roadRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkDriverStatuses() {
        List<Driver> drivers = driverRepository.findAll();
        synchronized (drivers) {
            for (Driver driver : drivers) {
                setStatusForDriver(driver);
            }
        }
    }

    public void setStatusForDriver(Driver driver) {
        Optional<Road> firstRoad = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driver.getId());

        if (firstRoad.isPresent()) {
            Road road = firstRoad.get();
            LocalDate today = LocalDate.now();

            if (today.isAfter(road.getDepartureDate()) && today.isBefore(road.getArrivalDate())) {
                driver.setDriverStatus(DriverStatus.ON_THE_WAY);
            } else {
                driver.setDriverStatus(DriverStatus.WAITING_FOR_ROAD);
            }
        } else {
            driver.setDriverStatus(DriverStatus.WAITING_FOR_ROAD);
        }
    }



}
