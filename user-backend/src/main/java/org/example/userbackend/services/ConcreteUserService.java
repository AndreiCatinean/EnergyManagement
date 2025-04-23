package org.example.userbackend.services;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.userbackend.controllers.handlers.exceptions.AuthenticationException;
import org.example.userbackend.controllers.handlers.exceptions.UserNotFoundException;
import org.example.userbackend.controllers.handlers.exceptions.UsernameTakenException;
import org.example.userbackend.dto.ChatUserDTO;
import org.example.userbackend.dto.LoginDTO;
import org.example.userbackend.dto.NewUserDTO;
import org.example.userbackend.dto.UserDTO;
import org.example.userbackend.entities.AppUser;
import org.example.userbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ConcreteUserService implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${DEVICE_URL}")
    private String deviceBackendURL;

    @Value("${CHAT_URL}")
    private String chatBackendURL;

    @Autowired
    public ConcreteUserService(UserRepository userRepository, RestTemplate restTemplate, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDTO authenticateUser(LoginDTO loginDTO) throws AuthenticationException {
        Optional<AppUser> user = userRepository.findByUsername(loginDTO.username());

        if (user.isEmpty() || !passwordEncoder.matches(loginDTO.password(), user.get().getPassword())) {
            throw new AuthenticationException();
        }
        AppUser foundUser = user.get();
        return new UserDTO(foundUser.getId(), foundUser.getUsername(), foundUser.isAdmin());
    }

    @Override
    @Transactional
    public void saveUser(NewUserDTO newUserDTO) throws UsernameTakenException {

        Optional<AppUser> user = userRepository.findByUsername(newUserDTO.username());
        if (user.isPresent())
            throw new UsernameTakenException();
        userRepository.save(toEntity(newUserDTO));

        Optional<AppUser> savedUser = userRepository.findByUsername(newUserDTO.username());
        if (!newUserDTO.isAdmin())
            savedUser.ifPresent(appUser -> restTemplate.exchange(deviceBackendURL, HttpMethod.POST, new HttpEntity<>(appUser.getId(), createHeadersWithAuth()), Void.class));

        savedUser.ifPresent(appUser -> restTemplate.exchange(chatBackendURL, HttpMethod.POST, new HttpEntity<>(new ChatUserDTO(appUser.getId(), appUser.getUsername(), appUser.isAdmin()), createHeadersWithAuth()), Void.class));
    }

    @Override
    public List<UserDTO> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(AppUser::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        Optional<AppUser> appUser = userRepository.findById(uuid);
        appUser.ifPresent(userRepository::delete);

        if (appUser.isPresent()) {
            restTemplate.exchange(chatBackendURL + "/?id=" + uuid, HttpMethod.DELETE, new HttpEntity<>(createHeadersWithAuth()), Void.class);
            if (!appUser.get().isAdmin())
                restTemplate.exchange(deviceBackendURL + "/?id=" + uuid, HttpMethod.DELETE, new HttpEntity<>(createHeadersWithAuth()), Void.class);
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        return AppUser.toDTO(userRepository.findByUsername(username).orElseThrow());
    }

    @Override
    @Transactional
    public void updateUser(UserDTO userDTO) {
        Optional<AppUser> appUser = userRepository.findById(userDTO.id());
        if (appUser.isEmpty())
            throw new UserNotFoundException();
        userRepository.updateUser(userDTO.id(), userDTO.username(), userDTO.isAdmin());
        restTemplate.exchange(chatBackendURL, HttpMethod.POST, new HttpEntity<>(new ChatUserDTO(userDTO.id(), userDTO.username(), userDTO.isAdmin()), createHeadersWithAuth()), Void.class);
    }


    private String getCookie() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getCredentials() instanceof Cookie) {
            return ((Cookie) authentication.getCredentials()).getValue();
        }
        return null;
    }

    private HttpHeaders createHeadersWithAuth() {
        String token = getCookie();
        HttpHeaders headers = new HttpHeaders();
        if (token != null) {

            headers.add("Cookie", "token=" + token);
        }
        return headers;
    }

    public AppUser toEntity(NewUserDTO userDTO) {
        return AppUser.builder()
                .username(userDTO.username())
                .password(passwordEncoder.encode(userDTO.password()))
                .isAdmin(userDTO.isAdmin())
                .build();
    }

}
