package org.example.devicebackend.dto;


import org.example.devicebackend.entities.Device;

import java.util.UUID;

public record DeviceDTO(UUID id, String description, String address, float hourlyConsumption, UUID ownerId) {
    public static DeviceDTO fromEntity(Device device) {
        UUID ownerId = null;
        if (device.getOwner() != null)
            ownerId = device.getOwner().getId();

        return new DeviceDTO(
                device.getId(),
                device.getDescription(),
                device.getAddress(),
                device.getHourlyConsumption(),
                ownerId
        );
    }
}
