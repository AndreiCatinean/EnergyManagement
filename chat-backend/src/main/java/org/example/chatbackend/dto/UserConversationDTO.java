package org.example.chatbackend.dto;

import java.util.UUID;

public record UserConversationDTO (UUID uuid, String username) {
}
