package com.example.pixsimulator.infra.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera todos os endpoints
                .allowedOrigins("http://localhost:8080", "http://localhost:4200", "https://swagger.io") // Origens permitidas
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Métodos permitidos
                .allowedHeaders("*") // Libera todos os cabeçalhos (importante para Content-Type)
                .allowCredentials(true);
    }
}
