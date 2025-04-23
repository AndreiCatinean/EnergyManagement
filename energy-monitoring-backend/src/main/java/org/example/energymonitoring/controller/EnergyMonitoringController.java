package org.example.energymonitoring.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energymonitoring.dto.HourConsumptionDTO;
import org.example.energymonitoring.service.energy_consumption.EnergyConsumptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/consumption")
@AllArgsConstructor
@Slf4j
public class EnergyMonitoringController {

    private final EnergyConsumptionService energyConsumptionService;

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    @GetMapping("/of-device/")
    public ResponseEntity<Map<Integer, HourConsumptionDTO>> getDeviceDayConsumption(
            @RequestParam("deviceId") UUID deviceId,
            @RequestParam("day") String day) {

        try {
            LocalDate selectedDay = LocalDate.parse(day);
            Map<Integer, HourConsumptionDTO> hourlyConsumption =
                    energyConsumptionService.getDeviceDayConsumption(deviceId, selectedDay);
            return ResponseEntity.ok(hourlyConsumption);
        } catch (Exception e) {
            log.error("Error fetching hourly consumption for device: {}", deviceId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
