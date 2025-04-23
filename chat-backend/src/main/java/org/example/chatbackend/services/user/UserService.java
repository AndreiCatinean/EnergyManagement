package org.example.chatbackend.services.user;

import org.example.chatbackend.dto.UserDTO;

import java.util.UUID;

public interface UserService {


    void saveUser(UserDTO userDTO);

    void deleteUser(UUID id);
}
