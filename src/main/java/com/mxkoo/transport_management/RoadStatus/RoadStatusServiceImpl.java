package com.mxkoo.transport_management.RoadStatus;

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

    @Scheduled(cron = "0 1 0 * * ?")
    public void checkRoadStatuses() {
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            setStatusForRoad(road);
        }
    }

    public void setStatusForRoad(Road road) {
        LocalDate today = LocalDate.now();

        if (!today.isBefore(road.getDepartureDate()) && !today.isAfter(road.getArrivalDate())) {
            road.setRoadStatus(RoadStatus.IN_PROGRESS);
        } else if (today.isBefore(road.getDepartureDate())) {
            road.setRoadStatus(RoadStatus.IN_FUTURE);
        } else if (today.isAfter(road.getArrivalDate())) {
            road.setRoadStatus(RoadStatus.DONE);
        }
        roadRepository.save(road);
    }

}
