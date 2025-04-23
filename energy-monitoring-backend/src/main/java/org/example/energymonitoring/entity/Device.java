package org.example.energymonitoring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Device implements Serializable {

    @Id
    private UUID id;

    @Column(name = "hourlyConsumption", nullable = false)
    private float hourlyConsumption;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<EnergyConsumption> measurements;

}
