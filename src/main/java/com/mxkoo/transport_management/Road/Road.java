package com.mxkoo.transport_management.Road;

import com.mxkoo.transport_management.Driver.Driver;
import com.mxkoo.transport_management.Truck.Truck;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;

import java.time.LocalDate;
import java.time.LocalDateTime;


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

    @NotBlank
    private LocalDate departureDate;
    @NotBlank
    private LocalDate arrivalDate;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    @ElementCollection
    private Driver driver;

    @Enumerated(EnumType.STRING)
    private RoadStatus roadStatus;
}
