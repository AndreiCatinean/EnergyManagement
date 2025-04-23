package org.example.energymonitoring.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationDispatcher {

    private SimpMessagingTemplate messagingTemplate;


    public void dispatchMessage(UUID deviceId, String message) {
        messagingTemplate.convertAndSend("/topic/notifications/" + deviceId, message);
    }
}
