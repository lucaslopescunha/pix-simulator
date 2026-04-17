package com.example.pixsimulator.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;
import java.time.LocalDateTime;

@Configuration
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

}
