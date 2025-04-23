package org.example.devicebackend.repository;

import org.example.devicebackend.entities.AppUser;
import org.example.devicebackend.entities.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> getDevicesByOwner(AppUser owner);

    @Modifying
    @Query("UPDATE Device d " +
            "SET d.description = :description, d.address = :address, d.hourlyConsumption = :hourlyConsumption, d.owner = :owner " +
            "WHERE d.id = :id")
    void updateDevice(
            @Param("id") UUID id,
            @Param("description") String description,
            @Param("address") String address,
            @Param("hourlyConsumption") float hourlyConsumption,
            @Param("owner") AppUser owner
    );
}
