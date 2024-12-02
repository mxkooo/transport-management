package com.mxkoo.transport_management.Road;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.RoadStatus.RoadStatus;
import com.mxkoo.transport_management.Truck.Truck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;


@Table(name = "ROAD")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Road {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "\"from\"")
    private String from;


    @Column(name = "\"via\"")
    private String[] via;

    @NotBlank
    @Column(name = "\"to\"")
    private String to;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    @JsonIgnore
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonIgnore
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RoadStatus roadStatus;
}
