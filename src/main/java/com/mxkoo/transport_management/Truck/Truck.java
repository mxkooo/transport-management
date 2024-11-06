package com.mxkoo.transport_management.Truck;

import com.mxkoo.transport_management.Coordinates.Coordinates;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.Date;


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

    private Date inspectionDate = new Date();

}
