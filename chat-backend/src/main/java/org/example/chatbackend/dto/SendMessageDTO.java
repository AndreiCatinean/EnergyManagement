package org.example.chatbackend.dto;

import java.util.UUID;

public record SendMessageDTO(UUID senderId, UUID conversationId, String content) {
}
