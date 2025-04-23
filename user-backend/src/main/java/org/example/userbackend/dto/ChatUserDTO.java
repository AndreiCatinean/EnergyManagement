package org.example.userbackend.dto;

import java.util.UUID;

public record ChatUserDTO(UUID uuid, String username ,boolean isAdmin) {
}
