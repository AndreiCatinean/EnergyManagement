package org.example.energymonitoring.service.energy_consumption;

import org.example.energymonitoring.dto.HourConsumptionDTO;
import org.example.energymonitoring.entity.Device;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface EnergyConsumptionService {
    Map<Integer, HourConsumptionDTO> getDeviceDayConsumption(UUID deviceId, LocalDate selectedDay);

    void checkConsumptionForHour(Device device, Instant timestamp);
}
