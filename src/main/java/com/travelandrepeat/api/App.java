package com.travelandrepeat.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication( exclude = DataSourceAutoConfiguration.class )
public class App {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(App.class, args);
        log.info("Application started with context: {}", context.getDisplayName());
        log.info("Travel & Repeat API is running...");
    }
}