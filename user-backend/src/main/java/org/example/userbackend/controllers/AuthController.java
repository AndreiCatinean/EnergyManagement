package org.example.userbackend.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userbackend.dto.LoginDTO;
import org.example.userbackend.dto.UserDTO;
import org.example.userbackend.security.JwtUtil;
import org.example.userbackend.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/login")
@AllArgsConstructor
@Slf4j
public class AuthController {


    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        UserDTO userDTO = userService.authenticateUser(loginDTO);
        String token = jwtUtil.generateToken(userDTO.username(), userDTO.isAdmin());
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok("Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUserFromCookie(HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getUserByUsername(jwtUtil.whoAmI(request)));
    }
}
