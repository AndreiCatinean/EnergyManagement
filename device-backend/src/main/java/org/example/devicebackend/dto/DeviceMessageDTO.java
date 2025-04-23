package org.example.devicebackend.dto;

import org.example.devicebackend.entities.Device;

import java.util.UUID;

public record DeviceMessageDTO(UUID deviceId, float hourlyConsumption, DeviceUpdateType deviceUpdateType) {

    public static DeviceMessageDTO getDeviceMessageDTO(Device device) {
        return new DeviceMessageDTO(device.getId(),
                device.getHourlyConsumption(),
                DeviceUpdateType.ADD_DEVICE);
    }
}
