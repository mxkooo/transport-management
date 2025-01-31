package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Driver.DriverMapper;
import com.mxkoo.transport_management.Driver.DriverService;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatus;
import com.mxkoo.transport_management.RoadStatus.RoadStatusService;
import com.mxkoo.transport_management.Truck.*;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@Service
@AllArgsConstructor
public class RoadServiceImpl implements RoadService {

    private RoadRepository roadRepository;
    private TruckService truckService;
    private DriverService driverService;
    private RoadStatusService roadStatusService;
    private RestTemplate restTemplate;
    private DriverMapper driverMapper;
    private RoadMapper roadMapper;
    private TruckMapper truckMapper;

    @Transactional
    public RoadDTO createRoad(RoadDTO roadDTO, int capacity){
        Truck truck = truckService.getAvailableTruck(capacity, roadDTO);
        Driver driver = driverService.getAvailableDriverNotOnRoad(roadDTO);

        if (!truck.getTruckStatus().equals(TruckStatus.WAITING_FOR_ROAD) || !driver.getDriverStatus().equals(DriverStatus.WAITING_FOR_ROAD)){
            throw new IllegalArgumentException("Pojazd lub kierowca nie jest gotowy do drogi");
        }
        validateDate(roadDTO.departureDate(), roadDTO.arrivalDate());
        Road road = new Road();
        road.setFrom(roadDTO.from());
        road.setVia(roadDTO.via());
        road.setTo(roadDTO.to());
        road.setDepartureDate(roadDTO.departureDate());
        road.setArrivalDate(roadDTO.arrivalDate());
        Double distance = calculateDistance(roadDTO.from(), roadDTO.via(), roadDTO.to());
        Double roundDistance = (double) (Math.round(distance * 100)/100);
        Double price = roundDistance * 7;
        road.setDistance(roundDistance);
        road.setPrice(price);
        road.setTruck(truck);
        road.setDriver(driver);
        roadStatusService.setStatusForRoad(road);
        return roadMapper.mapToDTO(roadRepository.save(road));
    }

    @Transactional
    public RoadDTO updateRoad(Long id, RoadDTO toUpdate){
        checkIfExists(id);
        Road road = roadMapper.mapToEntity(getRoadById(id));
        if (ChronoUnit.DAYS.between(LocalDate.now(), road.getDepartureDate()) < 7){
            throw new IllegalArgumentException("Można edytować trasę do 7 dni przed wyjazdem");
        }

        if (toUpdate.from() != null) {
            road.setFrom(toUpdate.from());
        }
        if  (toUpdate.via() != null) {
            road.setVia(toUpdate.via());
        }
        if (toUpdate.to() != null) {
            road.setTo(toUpdate.to());
        }
        if(toUpdate.departureDate() != null){
            road.setDepartureDate(toUpdate.departureDate());
        }
        if(toUpdate.arrivalDate() != null){
            road.setArrivalDate(toUpdate.arrivalDate());
        }
        if(toUpdate.truckDTO() != null){
            road.setTruck(truckMapper.mapToEntityWithRoad(toUpdate.truckDTO()));
        }
        if(toUpdate.driverDTO() != null){
            road.setDriver(driverMapper.mapToEntityWithRoad(toUpdate.driverDTO()));
        }
        if (toUpdate.roadStatus() != null){
            road.setRoadStatus(toUpdate.roadStatus());
        }
        return roadMapper.mapToDTO(roadRepository.save(road));
    }
    @Transactional
    public List<RoadDTO> getAllRoads(){
        List<Road> roads = roadRepository.findAll();
        return roads.stream()
                .map(roadMapper::mapToDTO)
                .toList();
    }
    @Transactional
    public void deleteAllRoads(){
        var roads = roadRepository.findAll();
        roadRepository.deleteAll(roads);
    }
    @Transactional
    public RoadDTO getRoadById(Long id){
        checkIfExists(id);
        return roadMapper.mapToDTO(roadRepository.findById(id).orElseThrow());
    }

    private void checkIfExists(Long id) {
        if (!roadRepository.existsById(id)){
            throw new NoSuchElementException("Road doesn't exist");
        }
    }
    @Transactional
    public double calculateDistance(String from, String[] via, String to) {
        List<String> allCities = new ArrayList<>();
        allCities.add(from);
        allCities.addAll(List.of(via));
        allCities.add(to);

        double totalDistance = 0;

        for (int i = 0; i < allCities.size() - 1; i++) {
            String origin = allCities.get(i);
            String destination = allCities.get(i + 1);

            totalDistance += calculateSegmentDistance(origin, destination);
        }

        return totalDistance/1000;
    }

    private double calculateSegmentDistance(String from, String to) {
        String url = String.format(
                "https://router.project-osrm.org/route/v1/driving/%s;%s?overview=false",
                geocodeCity(from), geocodeCity(to)
        );

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getBody() != null) {
            List<Map<String, Object>> routes = (List<Map<String, Object>>) response.getBody().get("routes");
            if (routes != null && !routes.isEmpty()) {
                return ((Number) routes.get(0).get("distance")).doubleValue();
            }
        }

        throw new IllegalStateException("Unable to calculate distance between " + from + " and " + to);
    }

    private String geocodeCity(String city) {
        String url = String.format("https://nominatim.openstreetmap.org/search?format=json&q=%s", city);

        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
        if (response.getBody() != null && !response.getBody().isEmpty()) {
            Map<String, Object> location = (Map<String, Object>) response.getBody().get(0);
            double lat = Double.parseDouble(location.get("lat").toString());
            double lon = Double.parseDouble(location.get("lon").toString());
            return lon + "," + lat; // Format: longitude,latitude
        }

        throw new IllegalStateException("Unable to geocode city: " + city);
    }


    private void validateDate(LocalDate departureDate, LocalDate arrivalDate){
        if (arrivalDate.isBefore(departureDate)){
            throw new DateTimeException("Data przyjazdu musi być po dacie wyjazdu");
        }
        if (departureDate.isAfter(arrivalDate)){
            throw new DateTimeException("Data wyjazdu musi być przed datą przyjazdu");
        }
        if(arrivalDate.isBefore(LocalDate.now()) || departureDate.isBefore(LocalDate.now())){
            throw new DateTimeException("Data wyjazdu lub przyjazdu nie może być przed datą dzisiejszą");
        }
    }
}
