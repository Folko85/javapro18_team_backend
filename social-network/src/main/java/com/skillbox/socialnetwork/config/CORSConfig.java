package com.skillbox.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    private final Environment environment;

    public CORSConfig(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        List<String> allowedOrigins = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (environment.getProperty("cors[" + i + "]") == null) {
                break;
            }
            allowedOrigins.add(environment.getProperty("cors[" + i + "]"));
        }

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(String[]::new))
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin")
                .allowCredentials(true);
    }
}