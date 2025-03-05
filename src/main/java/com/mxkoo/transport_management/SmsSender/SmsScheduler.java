package com.mxkoo.transport_management.SmsSender;

import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@AllArgsConstructor
@Service
public class SmsScheduler {
    private final RoadRepository roadRepository;
    private final SmsSender smsSender;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkRoads(){
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) == 1){
                smsSender.createAndSendSMS(road.getDriver());
            }
        }

    }
}
