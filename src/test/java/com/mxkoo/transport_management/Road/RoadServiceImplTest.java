package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverDTO;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Driver.DriverService;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.RoadStatus.RoadStatus;
import com.mxkoo.transport_management.RoadStatus.RoadStatusService;
import com.mxkoo.transport_management.Truck.Truck;
import com.mxkoo.transport_management.Truck.TruckDTO;
import com.mxkoo.transport_management.Truck.TruckMapper;
import com.mxkoo.transport_management.Truck.TruckService;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoadServiceImplTest {
    private RoadRepository roadRepository;
    private RoadStatusService roadStatusService;
    private TruckService truckService;
    private DriverService driverService;
    private RestTemplate restTemplate;
    private RoadService roadService;

    @BeforeEach
    void prepare(){
        roadRepository = mock(RoadRepository.class);
        roadStatusService = mock(RoadStatusService.class);
        truckService = mock(TruckService.class);
        driverService = mock(DriverService.class);
        restTemplate = mock(RestTemplate.class);
        roadService = new RoadServiceImpl(roadRepository,truckService, driverService, roadStatusService, restTemplate);
    }


    @Test
    void createRoad() {
        //given
        TruckDTO truckDTO = new TruckDTO(1L, "XD 1234A", 55, new Coordinates(50,50),
                LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);

        DriverDTO driverDTO = new DriverDTO(1L, "Leo", "Messi", new Coordinates(50, 50),
                "messi@mail.com", 12345663L, new ArrayList<>(),
                DriverStatus.WAITING_FOR_ROAD, 5, null);

        RoadDTO roadDTO = new RoadDTO(1L, "Warszawa", new String[]{"Bydgoszcz"}, "Gdańsk",
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20),
                400.88, 2800.98, truckDTO, driverDTO, RoadStatus.IN_FUTURE);

        Truck mappedTruck = TruckMapper.mapToEntityWithRoad(truckDTO);
        Driver mappedDriver = DriverMapper.mapToEntityWithRoad(driverDTO);

        when(truckService.getAvailableTruck(55, roadDTO)).thenReturn(mappedTruck);
        when(driverService.getAvailableDriverNotOnRoad(roadDTO)).thenReturn(mappedDriver);

        System.out.println("Truck found: " + mappedTruck);
        System.out.println("Driver found: " + mappedDriver);

        assertNotNull(mappedTruck, "Truck should not be null!");
        assertNotNull(mappedDriver, "Driver should not be null!");

        Road created = new Road();
        created.setId(1L);
        created.setFrom("Warszawa");
        created.setVia(new String[]{"Bydgoszcz"});
        created.setTo("Gdańsk");
        created.setDepartureDate(LocalDate.of(2025,5,5));
        created.setArrivalDate(LocalDate.of(2025,5,20));
        created.setDistance(400.88);
        created.setPrice(2800.98);
        created.setTruck(mappedTruck);
        created.setDriver(mappedDriver);
        created.setRoadStatus(RoadStatus.IN_FUTURE);

        when(roadRepository.save(any(Road.class))).thenReturn(created);

        //when
        RoadDTO createdDTO = roadService.createRoad(roadDTO, 55);

        //then
        assertAll("Mapping",
                () -> assertEquals(roadDTO.id(), createdDTO.id()),
                () -> assertEquals(roadDTO.from(), createdDTO.from()),
                () -> assertEquals(roadDTO.via(), createdDTO.via()),
                () -> assertEquals(roadDTO.to(), createdDTO.to()),
                () -> assertEquals(roadDTO.departureDate(), createdDTO.departureDate()),
                () -> assertEquals(roadDTO.arrivalDate(), createdDTO.arrivalDate()),
                () -> assertEquals(roadDTO.distance(), createdDTO.distance()),
                () -> assertEquals(roadDTO.price(), createdDTO.price()),
                () -> assertEquals(roadDTO.truckDTO(), createdDTO.truckDTO()),
                () -> assertEquals(roadDTO.driverDTO(), createdDTO.driverDTO()),
                () -> assertEquals(roadDTO.roadStatus(), createdDTO.roadStatus())
        );
    }


}