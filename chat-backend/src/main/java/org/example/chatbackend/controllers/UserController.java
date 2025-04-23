package org.example.chatbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.chatbackend.dto.UserDTO;
import org.example.chatbackend.services.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping(value = "/user")
@AllArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody UserDTO userDTO) {
        log.info("Saving user " + userDTO.uuid());
        userService.saveUser(userDTO);
    }


    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@RequestParam("id") UUID id) {
        log.info("Deleting user with id " + id);
        userService.deleteUser(id);
    }
}
