package org.example.chatbackend.dto;


import java.util.List;
import java.util.UUID;


public record ConversationDTO(UUID id, List<UserConversationDTO> participants
) {
}
