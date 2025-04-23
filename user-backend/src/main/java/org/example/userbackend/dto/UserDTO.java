package org.example.userbackend.dto;

import java.util.UUID;

public record UserDTO(UUID id, String username, boolean isAdmin) {
}
