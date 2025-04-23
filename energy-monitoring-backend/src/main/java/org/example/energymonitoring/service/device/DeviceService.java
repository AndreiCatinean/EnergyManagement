package org.example.energymonitoring.service.device;

import org.example.energymonitoring.dto.DeviceChangeDTO;

public interface DeviceService {
    void executeDeviceChange(DeviceChangeDTO deviceChangeDTO);

}
