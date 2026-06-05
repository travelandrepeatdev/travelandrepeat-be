package com.travelandrepeat.api;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
public class App implements ApplicationRunner {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        log.info("Application started with context: {}", context.getDisplayName());
        log.warn("✈ Travel & Repeat API is running now!");
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        log.warn("✈ Starting Travel & Repeat API!");
    }
}