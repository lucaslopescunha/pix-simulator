package com.example.pixsimulator.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

//    @Bean
    public OpenAPI pixOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Pix Simulator API").version("v1")
                              .description("Simulador de pix"));
    }
}
