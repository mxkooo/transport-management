package com.mxkoo.transport_management.Leave;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.mxkoo.transport_management.Driver.Driver;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "LEAVE")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "driver_id")
    @JsonBackReference
    private Driver driver;

    @Column(name = "\"start\"")
    private LocalDate start;

    @Column(name = "\"end\"")
    private LocalDate end;



}
