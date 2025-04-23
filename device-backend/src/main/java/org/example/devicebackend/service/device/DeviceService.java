package org.example.devicebackend.service.device;

import org.example.devicebackend.dto.DeviceDTO;
import org.example.devicebackend.dto.NewDeviceDTO;
import org.example.devicebackend.entities.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    List<DeviceDTO> getAll();

    void saveDevice(NewDeviceDTO deviceDTO);

    void deleteDevice(UUID deviceId);

    void updateDevice(DeviceDTO deviceDTO);

    Device toEntity(NewDeviceDTO newDeviceDTO);

    List<DeviceDTO> getByOwnerId(UUID ownerId);
}
