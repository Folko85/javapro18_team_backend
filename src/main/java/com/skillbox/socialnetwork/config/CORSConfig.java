package com.skillbox.socialnetwork.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//        ;
                .allowedOrigins("http://localhost:8086/", "http://127.0.0.1:8086/", "http://34.118.44.199:8086/", "http://localhost/", "http://127.0.0.1/", "http://195.234.208.58/", "http://localhost:80/", "http://127.0.0.1:80/", "http://195.234.208.58:80/", "http://10.186.0.2:80/", "http://10.186.0.2:8086/", "http://10.186.0.2/%22", "http://www.zeronenetwork.design:80/", "http://www.zeronenetwork.design:8086/")
//                .allowedOriginPatterns("/**")
                        .allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Cache-Control", "Content-Type", "Access-Control-Allow-Origin")
                        .allowCredentials(true);
    }
}