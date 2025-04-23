package org.example.userbackend.services;

import org.example.userbackend.controllers.handlers.exceptions.AuthenticationException;
import org.example.userbackend.controllers.handlers.exceptions.UserNotFoundException;
import org.example.userbackend.controllers.handlers.exceptions.UsernameTakenException;
import org.example.userbackend.dto.LoginDTO;
import org.example.userbackend.dto.NewUserDTO;
import org.example.userbackend.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO authenticateUser(LoginDTO loginDTO) throws AuthenticationException;

    void saveUser(NewUserDTO newUserDTO) throws UsernameTakenException;

    List<UserDTO> getUsers();

    void deleteUser(UUID uuid);

    UserDTO getUserByUsername(String username);

    void updateUser(UserDTO userDTO) throws UserNotFoundException;
}
