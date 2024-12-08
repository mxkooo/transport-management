package com.mxkoo.transport_management.Email;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverService;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Road.RoadRepository;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender mailSender;
    private final RoadRepository roadRepository;
    private final DriverService driverService;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkRoads() throws Exception {
        List<Road> roads = roadRepository.findAll();
        synchronized (roads) {
            for (Road road : roads) {
                if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) == 3){
                    sendDriverNotification(road.getDriver().getId());
                }

            }
        }
    }

    public void sendDriverNotification(Long driverId) throws Exception {
        Driver driver = driverService.getDriverById(driverId);

        SimpleMailMessage email = createEmail(driver);
        mailSender.send(email);
    }

    private  SimpleMailMessage createEmail(Driver driver) {
        SimpleMailMessage email = new SimpleMailMessage();
        String destination = roadRepository.findFirstByDriverIdOrderByDepartureDateAsc(driver.getId()).get().getTo();
        email.setTo(driver.getEmail());
        email.setSubject("3 DNI DO TRASY DO " + destination);
        email.setText("Zostały 3 dni do twojej najblizszej trasy do" + destination);
        return email;
    }

}
