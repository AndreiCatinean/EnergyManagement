package org.example.devicebackend.controller.handlers.exceptions;

public class DeviceNotFoundException extends RuntimeException {

    private static final String DEVICE_NOT_FOUND_EXCEPTION = "Device not found";


    public DeviceNotFoundException() {
        super(DEVICE_NOT_FOUND_EXCEPTION);
    }
}
