package org.example.devicebackend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class AppUser implements Serializable {

    @Id
    private UUID id;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Device> deviceList;


}
