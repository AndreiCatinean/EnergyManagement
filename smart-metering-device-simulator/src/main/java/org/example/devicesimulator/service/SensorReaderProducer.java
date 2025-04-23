package org.example.devicesimulator.service;

import jakarta.annotation.PostConstruct;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.devicesimulator.ConfigClass;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Setter
@Component
public class SensorReaderProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${DEVICE_ID:dev1}")
    private String deviceId;

    private List<String> csvData = new ArrayList<>();
    private int currentIndex = 0;

    private LocalDateTime currentTime;
    private int measurementsPerHour = 6;
    private int minutesPerRead = 10;

    @Autowired
    public SensorReaderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostConstruct
    public void init() {
        if (deviceId == null)
            throw new RuntimeException("No device id has been set");
        log.info("Initializing SensorReaderProducer with DEVICE_ID: {}", deviceId);

        this.currentTime = LocalDateTime.of(2024, 10, 10, 0, 0, 0, 0);
        setCsvData(deviceId);
    }

    public void setCsvData(String deviceId) {
        Path path = Path.of(ConfigClass.SENSORS_PATH + deviceId + ".csv");

        System.out.println(path);

        try {
            this.csvData = Files.readAllLines(path);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @Scheduled(fixedRate = 5000)
    public void sendMeasurement() {

        if (currentIndex >= csvData.size()) return;

        String line = csvData.get(currentIndex++);
        float measurementValue = Float.parseFloat(line.trim());

        String message = String.format(
                "{\"timestamp\": \"%d\", \"deviceId\": \"%s\", \"measurementValue\": %.2f}",
                getSimulatedTimestamp(), deviceId, measurementValue
        );

        rabbitTemplate.convertAndSend(ConfigClass.EXCHANGE_NAME, ConfigClass.ROUTING_KEY, message);
        log.info("Sent message: " + message);

        if (currentIndex % measurementsPerHour == 0) {
            currentTime = currentTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        } else {
            currentTime = currentTime.plusMinutes(minutesPerRead);
        }
    }

    private long getSimulatedTimestamp() {
        return currentTime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
