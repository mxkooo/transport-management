package com.mxkoo.transport_management.Truck.TruckStatus;

import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import com.mxkoo.transport_management.Truck.Truck;
import com.mxkoo.transport_management.Truck.TruckRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TruckStatusServiceImpl implements TruckStatusService{
    private final TruckRepository truckRepository;
    private final RoadRepository roadRepository;

    @Scheduled(cron = "0 1 0 * * ?")
    public void checkTruckStatuses() {
        List<Truck> trucks = truckRepository.findAll();
        synchronized (trucks) {
            for (Truck truck : trucks) {
                setStatusForTruck(truck);
                truckRepository.save(truck);
            }
        }
    }

    public void setStatusForTruck(Truck truck) {
        Optional<Road> firstRoad = roadRepository.findFirstByTruckIdOrderByDepartureDateAsc(truck.getId());

        if (firstRoad.isPresent()) {
            Road road = firstRoad.get();
            LocalDate today = LocalDate.now();

            if (!today.isBefore(road.getDepartureDate()) && !today.isAfter(road.getArrivalDate())) {
                truck.setTruckStatus(TruckStatus.ON_THE_WAY);
            } else {
                truck.setTruckStatus(TruckStatus.WAITING_FOR_ROAD);
            }
        } else {
            truck.setTruckStatus(TruckStatus.WAITING_FOR_ROAD);
        }
    }

}
