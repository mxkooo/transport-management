package com.mxkoo.transport_management.RoadStatus;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverRepository;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.Road.Road;

import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RoadStatusServiceImpl implements RoadStatusService {

    private final RoadRepository roadRepository;
    private final DriverRepository driverRepository;

//    private final List<Road> roads;
//    @Autowired
//    public RoadStatusServiceImpl(RoadRepository roadRepository) {
//        this.roadRepository = roadRepository;
//        this.roads = roadRepository.findAll();
//    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkRoadStatuses() {
        List<Road> roads = roadRepository.findAll();
        synchronized (roads) {
            for (Road road : roads) {
                if (LocalDate.now().isAfter(road.getDepartureDate()) && LocalDate.now().isBefore(road.getArrivalDate())) {
                    setStatusForRoad(road);
                }

            }
        }
    }


    public void setStatusForRoad(Road road) {
        if (LocalDate.now().isAfter(road.getDepartureDate()) && LocalDate.now().isBefore(road.getArrivalDate())) {
            road.setRoadStatus(RoadStatus.IN_PROGRESS);
        }
        if (LocalDate.now().isBefore(road.getDepartureDate())){
            road.setRoadStatus(RoadStatus.IN_FUTURE);
        }
        if (LocalDate.now().isAfter(road.getArrivalDate())){
            road.setRoadStatus(RoadStatus.DONE);
        }
    }
}
