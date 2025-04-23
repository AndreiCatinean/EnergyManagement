package org.example.chatbackend.dto;


import java.util.UUID;

public record UserDTO(UUID uuid, String username, boolean isAdmin) {


}
