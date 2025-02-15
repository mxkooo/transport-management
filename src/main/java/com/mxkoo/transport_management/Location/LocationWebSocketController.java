package com.mxkoo.transport_management.Location;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class LocationWebSocketController {
    @MessageMapping("/location")
    @SendTo("/topic/locations")
    public Coordinates sendLocation(Coordinates coordinates) {
        System.out.println("Received coordinates: x=" + coordinates.getX() + ", y=" + coordinates.getY());
        return coordinates;
    }
}
