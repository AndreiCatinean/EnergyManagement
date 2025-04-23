package org.example.chatbackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.chatbackend.dto.ReadDTO;
import org.example.chatbackend.dto.SendMessageDTO;
import org.example.chatbackend.dto.TypingDTO;
import org.example.chatbackend.services.message.MessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class WebSocketController {

    private final ObjectMapper objectMapper;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;


    @MessageMapping("/send-message")
    public void sendMessage(String sendMessageDTO) throws JsonProcessingException {
        System.out.println(sendMessageDTO);
        messageService.sendMessage(objectMapper.readValue(sendMessageDTO, SendMessageDTO.class));
    }

    @MessageMapping("/mark-read")
    public void markMessagesAsRead(String readDTO) throws JsonProcessingException {
        System.out.println(readDTO);
        messageService.markMessagesAsRead(objectMapper.readValue(readDTO, ReadDTO.class));
    }

    @MessageMapping("/typing")
    public void typingNotification(String typingDTO) throws JsonProcessingException {
        System.out.println(typingDTO);
        simpMessagingTemplate.convertAndSend("/queue/typing/", objectMapper.readValue(typingDTO, TypingDTO.class));
    }
}
