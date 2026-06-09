package com.skillbox.socialnetwork.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Настройки редиса.
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "services.redis")
public class RedisProperties {

    private String host;
    private int port;
}
