package com.mxkoo.transport_management.RoadStatus;

import com.mxkoo.transport_management.Road.Road;

import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
//@AllArgsConstructor
public class RoadStatusServiceImpl implements RoadStatusService {

    private RoadRepository roadRepository;

    private final List<Road> roads;
    @Autowired
    public RoadStatusServiceImpl(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
        this.roads = roadRepository.findAll();
    }
    @Scheduled(cron = "0 0 * * * *")
    public void checkRoadStatuses() {
        synchronized (roads) {
            for (Road road : roads) {
                if (LocalDate.now().isAfter(road.getDepartureDate()) && LocalDate.now().isBefore(road.getArrivalDate())) {
                    road.setRoadStatus(RoadStatus.IN_PROGRESS);
                }

            }


        }
    }

    public void setStatusForRoad(LocalDate departureDate, LocalDate arrivalDate, Road road) {
        if (LocalDate.now().isAfter(departureDate) && LocalDate.now().isBefore(arrivalDate)) {
            road.setRoadStatus(RoadStatus.IN_PROGRESS);
        }
    }
}
