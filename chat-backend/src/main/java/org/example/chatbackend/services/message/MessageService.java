package org.example.chatbackend.services.message;

import org.example.chatbackend.dto.MessageDTO;
import org.example.chatbackend.dto.ReadDTO;
import org.example.chatbackend.dto.SendMessageDTO;
import org.example.chatbackend.entities.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    void sendMessage(SendMessageDTO messageDTO);

    List<MessageDTO> getMessagesByConversation(UUID conversationId);


    void markMessagesAsRead(ReadDTO readDTO);
}
