package org.example.devicebackend.service.device;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.devicebackend.controller.handlers.exceptions.DeviceNotFoundException;
import org.example.devicebackend.controller.handlers.exceptions.UserNotFoundException;
import org.example.devicebackend.dto.DeviceDTO;
import org.example.devicebackend.dto.DeviceMessageDTO;
import org.example.devicebackend.dto.DeviceUpdateType;
import org.example.devicebackend.dto.NewDeviceDTO;
import org.example.devicebackend.entities.AppUser;
import org.example.devicebackend.entities.Device;
import org.example.devicebackend.repository.DeviceRepository;
import org.example.devicebackend.repository.UserRepository;
import org.example.devicebackend.service.DeviceUpdateProducer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConcreteDeviceService implements DeviceService {

    DeviceRepository deviceRepository;
    UserRepository userRepository;
    DeviceUpdateProducer deviceUpdateProducer;

    @Override
    public List<DeviceDTO> getAll() {
        return deviceRepository.findAll()
                .stream()
                .map(DeviceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveDevice(NewDeviceDTO newDeviceDTO) {
        Device device = deviceRepository.save(toEntity(newDeviceDTO));
        deviceUpdateProducer.
                sendDeviceUpdate(DeviceMessageDTO.getDeviceMessageDTO(device));
    }

    @Override
    public void deleteDevice(UUID deviceId) {
        Optional<Device> device = deviceRepository.findById(deviceId);
        device.ifPresent(value -> deviceRepository.delete(value));
        deviceUpdateProducer.sendDeviceUpdate(
                new DeviceMessageDTO(deviceId,
                        0,
                        DeviceUpdateType.DELETE_DEVICE));
    }

    @Override
    @Transactional
    public void updateDevice(DeviceDTO deviceDTO) {
        deviceRepository.findById(deviceDTO.id())
                .orElseThrow(DeviceNotFoundException::new);
        AppUser owner = null;
        if (deviceDTO.ownerId() != null)
            owner = userRepository.findById(deviceDTO.ownerId())
                    .orElseThrow(UserNotFoundException::new);
        deviceRepository.updateDevice(deviceDTO.id(), deviceDTO.description(), deviceDTO.address(), deviceDTO.hourlyConsumption(), owner);
        deviceUpdateProducer.
                sendDeviceUpdate(new DeviceMessageDTO(deviceDTO.id(), deviceDTO.hourlyConsumption(), DeviceUpdateType.ADD_DEVICE));
    }

    @Override
    public List<DeviceDTO> getByOwnerId(UUID ownerId) {
        AppUser owner = userRepository.findById(ownerId)
                .orElseThrow(UserNotFoundException::new);

        return deviceRepository.getDevicesByOwner(owner)
                .stream()
                .map(DeviceDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public Device toEntity(NewDeviceDTO newDeviceDTO) {
        AppUser owner = null;
        if (newDeviceDTO.ownerId() != null)
            owner = userRepository.findById(newDeviceDTO.ownerId())
                    .orElseThrow(UserNotFoundException::new);

        return new Device(
                newDeviceDTO.description(),
                newDeviceDTO.address(),
                newDeviceDTO.hourlyConsumption(),
                owner
        );
    }

}
