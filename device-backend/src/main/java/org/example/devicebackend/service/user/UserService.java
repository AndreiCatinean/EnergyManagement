package org.example.devicebackend.service.user;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void saveUser(UUID id);

    void deleteUser(UUID id);

    List<UUID> getAll();
}
