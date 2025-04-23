package org.example.devicebackend.service;

import lombok.extern.slf4j.Slf4j;
import org.example.devicebackend.Config;
import org.example.devicebackend.dto.DeviceMessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceUpdateProducer {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public DeviceUpdateProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendDeviceUpdate(DeviceMessageDTO deviceMessageDTO) {
        String message = String.format(
                "{\"deviceId\": \"%s\", \"deviceChangeType\": \"%s\", \"hourlyConsumption\": %s}",
                deviceMessageDTO.deviceId(), deviceMessageDTO.deviceUpdateType(), deviceMessageDTO.hourlyConsumption()
        );
        rabbitTemplate.convertAndSend(
                Config.DEVICE_EXCHANGE_NAME,
                Config.DEVICE_ROUTING_KEY,
                message
        );
        log.info("Sent device update message: {}", message);
    }
}
