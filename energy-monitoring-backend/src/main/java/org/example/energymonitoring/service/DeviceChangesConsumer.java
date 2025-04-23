package org.example.energymonitoring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energymonitoring.ConfigClass;
import org.example.energymonitoring.dto.DeviceChangeDTO;
import org.example.energymonitoring.service.device.DeviceService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DeviceChangesConsumer {

    private final DeviceService deviceService;

    @RabbitListener(queues = ConfigClass.DEVICE_QUEUE_NAME)
    public void receiveDeviceUpdate(String message) {
        log.info("Received device update message: {}", message);
        try {
            DeviceChangeDTO deviceChangeDTO = parseMessage(message);
            if (deviceChangeDTO != null) {
                deviceService.executeDeviceChange(deviceChangeDTO);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private DeviceChangeDTO parseMessage(String message) throws JsonProcessingException {
        return new ObjectMapper().readValue(message, DeviceChangeDTO.class);
    }
}