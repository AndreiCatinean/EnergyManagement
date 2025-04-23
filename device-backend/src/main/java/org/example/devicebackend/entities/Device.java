package org.example.devicebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.devicebackend.dto.DeviceMessageDTO;
import org.example.devicebackend.dto.DeviceUpdateType;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "hourlyConsumption", nullable = false)
    private float hourlyConsumption;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private AppUser owner;


    public Device(String description, String address, float hourlyConsumption, AppUser owner) {
        this.description = description;
        this.address = address;
        this.hourlyConsumption = hourlyConsumption;
        this.owner = owner;
    }


}
