package org.example.devicebackend;

import lombok.AllArgsConstructor;
import org.example.devicebackend.security.JwtAuthenticationFilter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class Config implements WebMvcConfigurer {

    public static final String DEVICE_QUEUE_NAME = "device-changes-queue";
    public static final String DEVICE_EXCHANGE_NAME = "device-changes-exchange";
    public static final String DEVICE_ROUTING_KEY = "device.changes";

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
        return BindingBuilder.bind(deviceChangesQueue)
                .to(deviceChangesExchange)
                .with(DEVICE_ROUTING_KEY);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
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

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOriginPatterns("*")
                .allowedHeaders("Content-Type", "Authorization")
                .allowCredentials(true);
    }
}