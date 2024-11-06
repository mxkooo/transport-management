package com.mxkoo.transport_management.Coordinates;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Embeddable
public class Coordinates {
    private int x;
    private int y;



}
