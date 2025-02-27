package com.mxkoo.transport_management.SmsSender;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
@Service
@PropertySource("classpath:application.properties")
public class SmsService{
    private RoadRepository roadRepository;
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;
    public SmsService(RoadRepository roadRepository) {
        this.roadRepository = roadRepository;
    }
    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void checkRoads(){
        List<Road> roads = roadRepository.findAll();
        for (Road road : roads) {
            if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) == 1){
                createAndSendSMS(road.getDriver());
            }
        }

    }

    private void createAndSendSMS(Driver driver) {
        String recipientNumber = driver.getContactNumber().toString();
        String twilioNumber = System.getenv("TWILIO_NUMBER");
        Optional<Road> roadOptional = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driver.getId());
        if (roadOptional.isPresent()) {
            String destination = roadOptional.get().getTo();
            Message.creator(
                            new PhoneNumber(recipientNumber),
                            new PhoneNumber(twilioNumber),
                            "Został 1 dzień do trasy do " + destination)
                    .create();
        }

    }


}
