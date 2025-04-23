package org.example.userbackend.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userbackend.dto.NewUserDTO;
import org.example.userbackend.dto.UserDTO;
import org.example.userbackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "/user")
@AllArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody NewUserDTO newUserDTO) {
        log.info("Saving user " + newUserDTO.username());
        userService.saveUser(newUserDTO);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
    public ResponseEntity<List<UserDTO>> getUsers() {
        log.info("Retrieving users");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUsers());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void updateUser(@RequestBody UserDTO userDTO) {
        log.info("Updating user with id " + userDTO.id());
        userService.updateUser(userDTO);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/")
    public void deleteUser(@RequestParam("id") UUID id) {
        log.info("Deleting user with id " + id);
        userService.deleteUser(id);
    }
}
