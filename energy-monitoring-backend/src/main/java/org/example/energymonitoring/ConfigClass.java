package org.example.energymonitoring;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.energymonitoring.security.JwtAuthenticationFilter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@Slf4j
@EnableWebSocketMessageBroker
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class ConfigClass implements WebSocketMessageBrokerConfigurer {

    public static final String QUEUE_NAME = "energy-data-queue";
    public static final String EXCHANGE_NAME = "energy-data-exchange";
    public static final String ROUTING_KEY = "energy.data";

    public static final String DEVICE_QUEUE_NAME = "device-changes-queue";
    public static final String DEVICE_EXCHANGE_NAME = "device-changes-exchange";
    public static final String DEVICE_ROUTING_KEY = "device.changes";


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
        return BindingBuilder.bind(energyDataQueue).to(energyDataExchange).with(ROUTING_KEY);
    }

    @Bean
    public Queue deviceChangesQueue() {
        return new Queue(DEVICE_QUEUE_NAME, true);
    }

    @Bean
    public DirectExchange deviceChangesExchange() {
        return new DirectExchange(DEVICE_EXCHANGE_NAME);
    }

    @Bean
    public Binding deviceChangesBinding(Queue deviceChangesQueue, DirectExchange deviceChangesExchange) {
        return BindingBuilder.bind(deviceChangesQueue).to(deviceChangesExchange).with(DEVICE_ROUTING_KEY);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors()
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
