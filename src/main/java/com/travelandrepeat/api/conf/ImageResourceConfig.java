package com.travelandrepeat.api.conf;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ImageResourceConfig implements WebMvcConfigurer {
    @Value("${env.local}")
    private boolean isLocal;

    @Value("${env.imageResource}")
    private String imageResource;

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        if (isLocal) {
            registry.addResourceHandler("/images/**").addResourceLocations(imageResource);
            return;
        }
        registry.addResourceHandler("/images/**").addResourceLocations("file:/uploads/");
    }
}
