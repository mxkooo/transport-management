package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceImplTest {

    private DriverRepository driverRepository;

    private DriverStatusService driverStatusService;

    private DriverServiceImpl driverService;

    @BeforeEach
    void prepare(){
        driverRepository = mock(DriverRepository.class);
        driverStatusService = mock(DriverStatusService.class);
        driverService = new DriverServiceImpl(driverRepository, driverStatusService);
    }

    @Test
    void createDriver() {
        // Given
        DriverDTO driverDTO = new DriverDTO(1L, "Leo", "Messi", new Coordinates(50, 50),
                "messi@mail.com", 12345663L, new ArrayList<>(),
                null, 5, null);

        Driver created = new Driver();
        created.setId(1L);
        created.setName("Leo");
        created.setLastName("Messi");
        created.setCoordinates(new Coordinates(50, 50));
        created.setEmail("messi@mail.com");
        created.setContactNumber(12345663L);
        created.setRoads(new ArrayList<>());
        created.setDaysOffLeft(5);
        created.setLeaves(null);

        when(driverRepository.save(any(Driver.class))).thenReturn(created);

        // When
        DriverDTO createdDTO = driverService.createDriver(driverDTO);

        // Then
        assertAll("Mapping",
                () -> assertEquals(driverDTO.id(), createdDTO.id()),
                () -> assertEquals(driverDTO.name(), createdDTO.name()),
                () -> assertEquals(driverDTO.lastName(), createdDTO.lastName()),
                () -> assertEquals(driverDTO.coordinates().getX(), createdDTO.coordinates().getX()),
                () -> assertEquals(driverDTO.coordinates().getY(), createdDTO.coordinates().getY()),
                () -> assertEquals(driverDTO.email(), createdDTO.email()),
                () -> assertEquals(driverDTO.contactNumber(), createdDTO.contactNumber()),
                () -> assertEquals(driverDTO.roads(), createdDTO.roads()),
                () -> assertEquals(driverDTO.daysOffLeft(), createdDTO.daysOffLeft()),
                () -> assertEquals(driverDTO.leaves(), createdDTO.leaves())
        );

        verify(driverRepository, times(1)).save(any(Driver.class));
    }

    @Test
    void updateDriver() throws Exception {
        Driver toUpdate = new Driver(1L, "Leo", "Messi", new Coordinates(50, 50),
                "messi@mail.com", 12345663L, new ArrayList<>(),
                null, 5, null);

        when(driverRepository.findById(eq(1L))).thenReturn(Optional.of(toUpdate));
        when(driverRepository.existsById(1L)).thenReturn(true);
        when(driverRepository.save(any(Driver.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        DriverDTO updated = driverService.updateDriver(1L, DriverMapper.mapToDTOWithRoad(toUpdate));

        // Then
        assertNotNull(toUpdate);
        assertNotNull(updated);
        assertEquals(toUpdate.getId(), updated.id());
        assertTrue(driverRepository.findById(1L).isPresent());

        verify(driverRepository, atLeastOnce()).findById(1L);
        verify(driverRepository).save(any(Driver.class));
    }
}
