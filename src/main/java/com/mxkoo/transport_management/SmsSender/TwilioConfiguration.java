package com.mxkoo.transport_management.SmsSender;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;
    @Bean
    public TwilioInitializer twilioInitializer() {
        return new TwilioInitializer(accountSid, authToken);
    }
}
