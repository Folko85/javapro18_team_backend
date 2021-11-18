package com.skillbox.socialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableRedisRepositories(basePackages = {"com.skillbox.socialnetwork.repository"})
public class NetworkApplication {
    public static void main(String[] args) {
        SpringApplication.run(NetworkApplication.class, args);
    }

}
