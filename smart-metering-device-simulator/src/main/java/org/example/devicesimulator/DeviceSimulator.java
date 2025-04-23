package org.example.devicesimulator;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableRabbit
@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan()

public class DeviceSimulator {

    public static void main(String[] args) {
        SpringApplication.run(DeviceSimulator.class, args);
    }
}
