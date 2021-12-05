package com.skillbox.socialnetwork.config;

import com.skillbox.socialnetwork.config.property.CORSProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class CORSConfig implements WebMvcConfigurer {

    private final CORSProperties properties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(properties.getUrls().toArray(String[]::new))
                .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin")
                .allowCredentials(true);
    }
}