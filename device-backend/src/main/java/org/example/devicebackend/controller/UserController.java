package org.example.devicebackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.devicebackend.service.user.UserService;
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

    UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UUID>> getUsers() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public void saveUser(@RequestBody UUID id) {
        log.info("Saving user with id " + id);
        userService.saveUser(id);
    }

    @DeleteMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@RequestParam("id") UUID id) {
        log.info("Deleting user with id " + id);
        userService.deleteUser(id);
    }
}
