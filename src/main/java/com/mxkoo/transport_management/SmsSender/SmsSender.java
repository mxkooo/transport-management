package com.mxkoo.transport_management.SmsSender;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SmsSender {
    private final RoadRepository roadRepository;

    public void createAndSendSMS(Driver driver) {
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
