package com.mxkoo.transport_management.Truck;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mxkoo.transport_management.Coordinates.Coordinates;
import com.mxkoo.transport_management.Road.Road;
import com.mxkoo.transport_management.Truck.TruckStatus.TruckStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Table(name = "TRUCK")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String licensePlate;

    private Integer capacity;

    @Embedded
    private Coordinates coordinates;

    private LocalDate inspectionDate;

    @OneToMany(mappedBy = "truck", cascade =  CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Road> roads = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TruckStatus truckStatus;
}
