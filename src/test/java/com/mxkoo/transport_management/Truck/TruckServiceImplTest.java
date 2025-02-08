package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.RoadDTO;
import com.mxkoo.transport_management.RoadStatus.RoadStatus;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TruckServiceImplTest {
    private TruckRepository truckRepository;
    private TruckService truckService;
    private TruckStatusService truckStatusService;

    @BeforeEach
    void prepare(){
        truckRepository = mock(TruckRepository.class);
        truckStatusService = mock(TruckStatusService.class);
        truckService = new TruckServiceImpl(truckRepository, truckStatusService);
    }

    @Test
    void createTruck(){
        //given
        TruckDTO truckDTO = new TruckDTO(1L, "XD 1234A", 55, new Coordinates(50,50), LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);

        Truck created = new Truck();
        created.setId(1L);
        created.setLicensePlate("XD 1234A");
        created.setCapacity(55);
        created.setCoordinates(new Coordinates(50,50));
        created.setInspectionDate(LocalDate.of(2025,12,12));
        created.setRoads(new ArrayList<>());
        created.setTruckStatus(TruckStatus.WAITING_FOR_ROAD);
        //when
        when(truckRepository.save(any(Truck.class))).thenReturn(created);

        TruckDTO createdDTO = truckService.createTruck(truckDTO);

        //then
        assertAll("Mapping",
                () -> assertEquals(truckDTO.id(), createdDTO.id()),
                () -> assertEquals(truckDTO.licensePlate(), createdDTO.licensePlate()),
                () -> assertEquals(truckDTO.capacity(), createdDTO.capacity()),
                () -> assertEquals(truckDTO.coordinates().getX(), createdDTO.coordinates().getX()),
                () -> assertEquals(truckDTO.coordinates().getY(), createdDTO.coordinates().getY()),
                () -> assertEquals(truckDTO.inspectionDate(), createdDTO.inspectionDate()),
                () -> assertEquals(truckDTO.roads(), createdDTO.roads()),
                () -> assertEquals(truckDTO.truckStatus(), createdDTO.truckStatus())
        );

        verify(truckRepository, times(1)).save(any(Truck.class));
    }

    @Test
    void updateTruck() throws Exception{
        //given
        Truck toUpdate = new Truck(1L, "XD 1234A", 55, new Coordinates(50,50), LocalDate.of(2025,12,12), new ArrayList<>(), TruckStatus.WAITING_FOR_ROAD);

        when(truckRepository.findById(eq(1L))).thenReturn(Optional.of(toUpdate));
        when(truckRepository.existsById(1L)).thenReturn(true);
        when(truckRepository.save(any(Truck.class))).thenAnswer(i -> i.getArgument(0));
        //when
        TruckDTO updated = truckService.updateTruck(1L, TruckMapper.mapToDTOWithRoad(toUpdate));
        //then
        assertNotNull(toUpdate);
        assertNotNull(updated);
        assertEquals(toUpdate.getId(), updated.id());
        assertTrue(truckRepository.findById(1L).isPresent());

        verify(truckRepository, atLeastOnce()).findById(1L);
        verify(truckRepository).save(any(Truck.class));

    }

    @Test
    void getTruck_WhenDoesNotExist(){
        //given
        Long id = 1L;

        //when
        when(truckRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> truckService.getTruckById(id));
        //then
        verify(truckRepository).findById(id);
    }

    @Test
    void getTruck_WhenIsOnTheWay_ShouldThrowException() {
        // given
        Truck truck = new Truck();
        truck.setCapacity(55);
        truck.setTruckStatus(TruckStatus.ON_THE_WAY);
        truck.setRoads(new ArrayList<>());
        RoadDTO roadDTO = new RoadDTO(1L, "Warszawa", new String[]{"Bydgoszcz"}, "Gdańsk",
                LocalDate.of(2025, 5, 5), LocalDate.of(2025, 5, 20),
                400.88, 2800.98, null, null, RoadStatus.IN_FUTURE);


        when(truckRepository.findByCapacityAndTruckStatus(55, TruckStatus.WAITING_FOR_ROAD))
                .thenReturn(Collections.emptyList());

        // when & then
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> truckService.getAvailableTruck(55, roadDTO));

        assertEquals("Nie znaleziono pojazdu", exception.getMessage());
    }



}