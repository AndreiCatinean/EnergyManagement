package org.example.devicebackend.dto;

import java.util.UUID;

public record NewDeviceDTO(String description, String address, float hourlyConsumption, UUID ownerId) {
}