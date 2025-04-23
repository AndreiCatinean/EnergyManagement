package org.example.chatbackend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDTO(
        UUID id,
        String content,
        LocalDateTime timestamp,
        UUID senderId,
        UUID conversationId,
        boolean read

) {
}
