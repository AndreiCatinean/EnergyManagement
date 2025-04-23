package org.example.devicebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.devicebackend.dto.DeviceDTO;
import org.example.devicebackend.dto.NewDeviceDTO;
import org.example.devicebackend.service.DeviceUpdateProducer;
import org.example.devicebackend.service.device.DeviceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/device")
@AllArgsConstructor
@Slf4j
public class DeviceController {

    DeviceService deviceService;
    DeviceUpdateProducer deviceUpdateProducer;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        log.info("Getting all devices");
        List<DeviceDTO> deviceDTOS = deviceService.getAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deviceDTOS);
    }

    @GetMapping("/of-user/")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<DeviceDTO>> getDevicesByOwner(@RequestParam("ownerId") UUID ownerId) {
        log.info("Getting all devices for user with id " + ownerId);
        List<DeviceDTO> deviceDTOS = deviceService.getByOwnerId(ownerId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(deviceDTOS);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveDevice(@RequestBody NewDeviceDTO newDeviceDTO) {
        log.info("Saving a new device " + newDeviceDTO);
        deviceService.saveDevice(newDeviceDTO);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteDevice(@RequestParam("deviceId") UUID deviceId) {
        log.info("Deleting device with id " + deviceId);
        deviceService.deleteDevice(deviceId);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public void updateUser(@RequestBody DeviceDTO deviceDTO) {
        log.info("Updating device with id " + deviceDTO.id());
        deviceService.updateDevice(deviceDTO);
    }
}
