package com.mxkoo.transport_management.SmsSender;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class TwilioInitializer {
    private final String accountSid;
    private final String authToken;


    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio initialized successfully with SID: {}", accountSid);
    }
}
