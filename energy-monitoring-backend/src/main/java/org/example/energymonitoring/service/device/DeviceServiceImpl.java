package org.example.energymonitoring.service.device;

import lombok.AllArgsConstructor;
import org.example.energymonitoring.dto.DeviceChangeDTO;
import org.example.energymonitoring.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class DeviceServiceImpl implements DeviceService {

    DeviceRepository deviceRepository;

    public void executeDeviceChange(DeviceChangeDTO deviceChangeDTO) {
        switch (deviceChangeDTO.deviceChangeType()) {
            case ADD_DEVICE -> addDevice(deviceChangeDTO);
            case DELETE_DEVICE -> deleteDevice(deviceChangeDTO);
        }
    }

    private void deleteDevice(DeviceChangeDTO deviceChangeDTO) {
        deviceRepository.deleteById(deviceChangeDTO.deviceId());
    }

    private void addDevice(DeviceChangeDTO deviceChangeDTO) {
        deviceRepository.save(DeviceChangeDTO.toEntity(deviceChangeDTO));
    }
}
