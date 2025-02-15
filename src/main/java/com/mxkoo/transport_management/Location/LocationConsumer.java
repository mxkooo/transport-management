package com.mxkoo.transport_management.Location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class LocationConsumer {
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "driver-location", groupId = "location-group")
    public void consumeLocation(String message){
        try{
            JsonNode jsonNode = objectMapper.readTree(message);
            Long driverId = jsonNode.get("driverId").asLong();
            Double x = jsonNode.get("x").asDouble();
            Double y = jsonNode.get("y").asDouble();

            String formattedMessage = String.format("{\"driverId\": %d, \"x\": %f, \"y\": %f}",
                    driverId, x, y);

            messagingTemplate.convertAndSend("/topic/locations", formattedMessage);
        }catch (Exception e){
            System.err.println("błąd " + e.getMessage());
        }

    }
}
