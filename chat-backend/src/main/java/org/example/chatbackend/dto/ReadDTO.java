package org.example.chatbackend.dto;

import java.util.UUID;

public record ReadDTO(UUID senderId, UUID conversationId) {
}
