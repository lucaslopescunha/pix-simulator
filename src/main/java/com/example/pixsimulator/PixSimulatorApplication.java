package com.example.pixsimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PixSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PixSimulatorApplication.class, args);
    }

}
