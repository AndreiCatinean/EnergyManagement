package org.example.devicebackend.service.user;

import lombok.AllArgsConstructor;
import org.example.devicebackend.entities.AppUser;
import org.example.devicebackend.entities.Device;
import org.example.devicebackend.repository.DeviceRepository;
import org.example.devicebackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ConcreteUserService implements UserService {

    UserRepository userRepository;
    DeviceRepository deviceRepository;

    @Override
    public List<UUID> getAll() {
        return userRepository.findAll()
                .stream()
                .map(AppUser::getId)
                .collect(Collectors.toList());
    }

    @Override
    public void saveUser(UUID id) {
        AppUser newUser = new AppUser();
        newUser.setId(id);
        userRepository.save(newUser);
    }

    @Override
    public void deleteUser(UUID id) {

        Optional<AppUser> user = userRepository.findById(id);

        if (user.isPresent()) {
            List<Device> userDevices = deviceRepository.getDevicesByOwner(user.get());
            for (Device device : userDevices) {
                device.setOwner(null);
                deviceRepository.save(device);
            }
            userRepository.delete(user.get());
        }

    }
}
