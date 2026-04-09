package com.church.programgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProgramGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProgramGeneratorApplication.class, args);
    }
}