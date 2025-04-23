package org.example.energymonitoring.dto;

import org.example.energymonitoring.entity.Device;

import java.util.UUID;

public record DeviceChangeDTO(UUID deviceId, float hourlyConsumption, DeviceChangeType deviceChangeType) {

    public static Device toEntity(DeviceChangeDTO deviceChangeDTO) {
        return Device.builder()
                .id(deviceChangeDTO.deviceId)
                .hourlyConsumption(deviceChangeDTO.hourlyConsumption())
                .build();
    }
}
