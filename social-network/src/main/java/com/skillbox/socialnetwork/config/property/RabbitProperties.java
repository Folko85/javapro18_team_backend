package com.skillbox.socialnetwork.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "services.rabbitmq")
public class RabbitProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private String exchange;
    private String routingKey;
    private String queue;
}
