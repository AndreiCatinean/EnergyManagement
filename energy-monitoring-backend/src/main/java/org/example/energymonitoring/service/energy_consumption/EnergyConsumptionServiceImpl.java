package org.example.energymonitoring.service.energy_consumption;

import lombok.AllArgsConstructor;
import org.example.energymonitoring.dto.HourConsumptionDTO;
import org.example.energymonitoring.entity.Device;
import org.example.energymonitoring.entity.EnergyConsumption;
import org.example.energymonitoring.repository.DeviceRepository;
import org.example.energymonitoring.repository.EnergyConsumptionRepository;
import org.example.energymonitoring.service.NotificationDispatcher;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    EnergyConsumptionRepository energyConsumptionRepository;
    DeviceRepository deviceRepository;
    NotificationDispatcher notificationDispatcher;

    public Map<Integer, HourConsumptionDTO> getDeviceDayConsumption(UUID deviceId, LocalDate day) {
        Instant startOfDay = day.atStartOfDay(ZoneId.of("UTC")).toInstant();
        Instant endOfDay = day.plusDays(1).atStartOfDay(ZoneId.of("UTC")).toInstant();

        Device device = deviceRepository.findById(deviceId).orElseThrow();

        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository
                .findTotalConsumptionFromInterval(deviceId, startOfDay, endOfDay);

        ZoneId targetZoneId = ZoneId.of("UTC");

        Map<Integer, List<EnergyConsumption>> groupedByHour = energyConsumptions.stream()
                .collect(Collectors.groupingBy(consumption -> {
                    ZonedDateTime zonedTimestamp = consumption.getTimestamp()
                            .atZone(ZoneId.systemDefault())
                            .withZoneSameInstant(targetZoneId);
                    return zonedTimestamp.getHour();
                }));

        Map<Integer, HourConsumptionDTO> hourlyConsumption = new HashMap<>();
        for (int hour = 0; hour < 24; hour++) {
            List<EnergyConsumption> hourData = groupedByHour.getOrDefault(hour, Collections.emptyList());

            if (hourData.size() < 2) {
                hourlyConsumption.put(hour, new HourConsumptionDTO(device.getHourlyConsumption(), 0));
            } else {
                float maxConsumption = hourData.stream()
                        .max(Comparator.comparing(EnergyConsumption::getConsumption))
                        .map(EnergyConsumption::getConsumption)
                        .orElse(0f);

                float minConsumption = hourData.stream()
                        .min(Comparator.comparing(EnergyConsumption::getConsumption))
                        .map(EnergyConsumption::getConsumption)
                        .orElse(0f);


                float actualConsumption = maxConsumption - minConsumption;

                hourlyConsumption.put(hour, new HourConsumptionDTO(device.getHourlyConsumption(), actualConsumption));
            }
        }
        return hourlyConsumption;
    }

    public void checkConsumptionForHour(Device device, Instant timestamp) {
        ZonedDateTime zonedDateTime = timestamp.atZone(ZoneId.of("UTC"));

        Instant startOfHour = zonedDateTime.withMinute(0).withSecond(0).withNano(0).toInstant();
        Instant endOfHour = startOfHour.plusSeconds(3600);

        List<EnergyConsumption> energyConsumptions = energyConsumptionRepository
                .findTotalConsumptionFromInterval(device.getId(), startOfHour, endOfHour);


        if (energyConsumptions.size() >= 2) {
            EnergyConsumption maxConsumption = energyConsumptions.stream()
                    .max(Comparator.comparing(EnergyConsumption::getTimestamp)).orElseThrow();

            EnergyConsumption
                    minConsumption = energyConsumptions.stream()
                    .min(Comparator.comparing(EnergyConsumption::getTimestamp))
                    .orElseThrow();

            float currentHourlyConsumption = (maxConsumption.getConsumption() - minConsumption.getConsumption());

            String message;
            if (currentHourlyConsumption > device.getHourlyConsumption()) {
                message = String.format("WARNING! Consumption/h: %.2f",
                        currentHourlyConsumption);
            } else {
                message = String.format("Consumption/h: %.2f",
                        currentHourlyConsumption);
            }
            notificationDispatcher.dispatchMessage(device.getId(), message);
        }
    }
}
