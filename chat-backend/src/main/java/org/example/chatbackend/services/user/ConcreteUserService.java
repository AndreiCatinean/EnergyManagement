package org.example.chatbackend.services.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatbackend.dto.UserDTO;
import org.example.chatbackend.entities.AppUser;
import org.example.chatbackend.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public class ConcreteUserService implements UserService {


    private final UserRepository userRepository;

    @Override
    public void saveUser(UserDTO userDTO) {
        userRepository.save(new AppUser(userDTO.uuid(), userDTO.username(), userDTO.isAdmin()));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.deleteById(id);
    }
}
