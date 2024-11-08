package com.mxkoo.transport_management.Coordinates;

import jakarta.persistence.Embeddable;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Coordinates {

    private double x;
    private double y;



}
