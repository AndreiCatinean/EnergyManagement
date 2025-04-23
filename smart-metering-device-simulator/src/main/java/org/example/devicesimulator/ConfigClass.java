package org.example.devicesimulator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ConfigClass {

    public static final String SENSORS_PATH = "../sensors/";
    public static final String QUEUE_NAME = "energy-data-queue";
    public static final String EXCHANGE_NAME = "energy-data-exchange";
    public static final String ROUTING_KEY = "energy.data";


    @Bean
    public Queue energyDataQueue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange energyDataExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue energyDataQueue, DirectExchange energyDataExchange) {
        return BindingBuilder.bind(energyDataQueue)
                .to(energyDataExchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
