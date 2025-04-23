package org.example.energymonitoring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energymonitoring.ConfigClass;
import org.example.energymonitoring.dto.EnergyConsumptionDTO;
import org.example.energymonitoring.entity.Device;
import org.example.energymonitoring.entity.EnergyConsumption;
import org.example.energymonitoring.repository.DeviceRepository;
import org.example.energymonitoring.repository.EnergyConsumptionRepository;
import org.example.energymonitoring.service.energy_consumption.EnergyConsumptionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@AllArgsConstructor

public class EnergyDataConsumer {


    private EnergyConsumptionRepository energyConsumptionRepository;
    private DeviceRepository deviceRepository;
    private EnergyConsumptionService energyConsumptionService;


    @RabbitListener(queues = ConfigClass.QUEUE_NAME)
    public void receiveMessage(String message) {
        try {
            EnergyConsumptionDTO measurement = parseMessage(message);
            if (measurement != null) {
                processMeasurement(measurement);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private EnergyConsumptionDTO parseMessage(String message) throws JsonProcessingException {
        return new ObjectMapper().readValue(message, EnergyConsumptionDTO.class);
    }

    private void processMeasurement(EnergyConsumptionDTO measurement) throws Exception {
        Device device = deviceRepository.findById(measurement.deviceId()).orElseThrow(() -> new Exception("Device not found"));

        EnergyConsumption consumption = EnergyConsumption.builder()
                .device(device)
                .timestamp(Instant.ofEpochMilli(measurement.timestamp()))
                .consumption(measurement.measurementValue())
                .build();

        energyConsumptionRepository.save(consumption);

        energyConsumptionService.checkConsumptionForHour(device, consumption.getTimestamp());
    }
}
