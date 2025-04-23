package org.example.energymonitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class EnergyConsumption implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;
    private Instant timestamp;
    private float consumption;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

}
