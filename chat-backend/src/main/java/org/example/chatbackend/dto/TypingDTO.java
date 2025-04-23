package org.example.chatbackend.dto;

import java.util.UUID;

public record TypingDTO (UUID conversationId, UUID receiverId) {
}
