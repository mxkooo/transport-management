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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.ParameterizedTypeReference;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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


//    @Test
//    void createRoad() {
//        // given
//        TruckDTO truckDTO = new TruckDTO(1L, "XD 1234A", 55, null, LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);
//        DriverDTO driverDTO = new DriverDTO(1L, "Leo", "Messi", null, "messi@mail.com", 12345663L, new ArrayList<>(), DriverStatus.WAITING_FOR_ROAD, 5, null);
//        RoadDTO roadDTO = new RoadDTO(1L, "Warszawa", new String[]{"Bydgoszcz"}, "Gdańsk", LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20), 400.88, 2800.98, truckDTO, driverDTO, RoadStatus.IN_FUTURE);
//
//        Truck mappedTruck = TruckMapper.mapToEntityWithRoad(truckDTO);
//        Driver mappedDriver = DriverMapper.mapToEntityWithRoad(driverDTO);
//        when(truckService.getAvailableTruck(55, roadDTO)).thenReturn(mappedTruck);
//        when(driverService.getAvailableDriverNotOnRoad(roadDTO)).thenReturn(mappedDriver);
//
//
//        ResponseEntity<List> responseEntity = new ResponseEntity<>(mockResponseBody, HttpStatus.OK);
//
//        when(restTemplate.getForEntity(anyString(), (Class<List>) any(Class.class)))
//                .thenReturn(responseEntity);
//
//
//        Road createdRoad = new Road();
//        createdRoad.setId(1L);
//        createdRoad.setFrom("Warszawa");
//        createdRoad.setVia(new String[]{"Bydgoszcz"});
//        createdRoad.setTo("Gdańsk");
//        createdRoad.setDepartureDate(LocalDate.of(2025,5,5));
//        createdRoad.setArrivalDate(LocalDate.of(2025,5,20));
//        createdRoad.setDistance(400.88);
//        createdRoad.setPrice(2800.98);
//        createdRoad.setTruck(mappedTruck);
//        createdRoad.setDriver(mappedDriver);
//        createdRoad.setRoadStatus(RoadStatus.IN_FUTURE);
//
//        when(roadRepository.save(any(Road.class))).thenReturn(createdRoad);
//
//        // when
//        RoadDTO createdDTO = roadService.createRoad(roadDTO, 55);
//
//        // then
//        assertAll("Mapping",
//                () -> assertEquals(roadDTO.id(), createdDTO.id()),
//                () -> assertEquals(roadDTO.from(), createdDTO.from()),
//                () -> assertEquals(roadDTO.via(), createdDTO.via()),
//                () -> assertEquals(roadDTO.to(), createdDTO.to()),
//                () -> assertEquals(roadDTO.departureDate(), createdDTO.departureDate()),
//                () -> assertEquals(roadDTO.arrivalDate(), createdDTO.arrivalDate()),
//                () -> assertEquals(roadDTO.distance(), createdDTO.distance()),
//                () -> assertEquals(roadDTO.price(), createdDTO.price()),
//                () -> assertEquals(roadDTO.truckDTO(), createdDTO.truckDTO()),
//                () -> assertEquals(roadDTO.driverDTO(), createdDTO.driverDTO()),
//                () -> assertEquals(roadDTO.roadStatus(), createdDTO.roadStatus())
//        );
//    }
    @Test
    void getAllTruckRoads(){
        //given
        Truck truck = new Truck(1L, "XD 1234A", 55, new Coordinates(50,50), LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);
        Driver driver = new Driver(1L, "Leo", "Messi", new Coordinates(50, 50),
                "messi@mail.com", 12345663L, new ArrayList<>(),
                DriverStatus.WAITING_FOR_ROAD, 5, null);
        Road road1 = new Road(1L, "Warszawa", new String[]{"Bydgoszcz"}, "Gdańsk",
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20),
                400.88, 2800.98, truck, driver, RoadStatus.IN_FUTURE);

        Road road2 = new Road(1L, "Lublin", new String[]{"Gliwice"}, "Opole",
                LocalDate.of(2025, 6, 5), LocalDate.of(2025, 6, 20),
                678.89, 5543.42, truck, driver, RoadStatus.IN_FUTURE);

        List<Road> roads = new ArrayList<>();
        roads.add(road1);
        roads.add(road2);
        //when
        when(roadRepository.getRoadByTruckId(1L)).thenReturn(roads);
        List<RoadDTO> result = roadService.getAllTruckRoads(1L);
        //then
        assertNotNull(result);
        assertEquals(roads.size(), result.size());

        for (int i = 0; i < roads.size(); i++) {
            assertEquals(roads.get(i).getId(), result.get(i).id());
        }

    }

    @Test
    void getAllDriverRoads(){
        //given
        Truck truck = new Truck(1L, "XD 1234A", 55, new Coordinates(50,50), LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);
        Driver driver = new Driver(1L, "Leo", "Messi", new Coordinates(50, 50),
                "messi@mail.com", 12345663L, new ArrayList<>(),
                DriverStatus.WAITING_FOR_ROAD, 5, null);
        Road road1 = new Road(1L, "Warszawa", new String[]{"Bydgoszcz"}, "Gdańsk",
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20),
                400.88, 2800.98, truck, driver, RoadStatus.IN_FUTURE);

        Road road2 = new Road(1L, "Lublin", new String[]{"Gliwice"}, "Opole",
                LocalDate.of(2025, 6, 5), LocalDate.of(2025, 6, 20),
                678.89, 5543.42, truck, driver, RoadStatus.IN_FUTURE);

        List<Road> roads = new ArrayList<>();
        roads.add(road1);
        roads.add(road2);

        //when
        when(roadRepository.getRoadByDriverId(1L)).thenReturn(roads);
        List<RoadDTO> result = roadService.getAllDriverRoads(1L);
        //then
        assertNotNull(result);
        assertEquals(roads.size(), result.size());

        for (int i = 0; i < roads.size(); i++) {
            assertEquals(roads.get(i).getId(), result.get(i).id());
        }
    }

}