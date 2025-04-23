package org.example.energymonitoring.dto;


import java.util.UUID;

public record EnergyConsumptionDTO(long timestamp, UUID deviceId, float measurementValue) {
}
