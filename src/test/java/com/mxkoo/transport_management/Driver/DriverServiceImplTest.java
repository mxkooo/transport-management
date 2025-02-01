package com.mxkoo.transport_management.Driver;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Driver.DriverStatus.DriverStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DriverServiceImplTest {

    private DriverRepository driverRepository;
    private DriverService driverService;
    private DriverStatusService driverStatusService;

    @BeforeEach
    void prepare(){
        driverRepository = mock(DriverRepository.class);
        driverService = new DriverServiceImpl(driverRepository, driverStatusService);
    }

    @Test
    void createDriver() {
        //given
        DriverDTO driverDTO = new DriverDTO(1l,"Leo", "Messi", new Coordinates(50,50), "messi@mail.com", 12345663L, null, null, 5, null);

        Driver created = new Driver();
        created.setId(1L);
        created.setName("Leo");
        created.setLastName("Messi");
        created.setCoordinates(new Coordinates(50,50));
        created.setEmail("messi@mail.com");
        created.setContactNumber(12345663L);
        created.setRoads(null);
        created.setDaysOffLeft(5);
        created.setLeaves(null);

        //when
        when(driverRepository.save(any(Driver.class))).thenReturn(created);

        DriverDTO createdDTO = driverService.createDriver(driverDTO);

        assertEquals(driverDTO.id(), createdDTO.id());
        assertEquals(driverDTO.name(), createdDTO.name());
        assertEquals(driverDTO.lastName(), createdDTO.lastName());
        assertEquals(driverDTO.coordinates(), createdDTO.coordinates());
        assertEquals(driverDTO.contactNumber(), createdDTO.contactNumber());
        assertEquals(driverDTO.roads(), createdDTO.roads());
        assertEquals(driverDTO.driverStatus(), createdDTO.driverStatus());
        assertEquals(driverDTO.daysOffLeft(), createdDTO.daysOffLeft());
        assertEquals(driverDTO.leaves(), createdDTO.leaves());

        verify(driverRepository, times(1)).save(any(Driver.class));

    }
}
