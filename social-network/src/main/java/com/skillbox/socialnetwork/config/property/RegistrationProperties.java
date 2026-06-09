package com.skillbox.socialnetwork.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Настройки подтверждения регистрации.
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "registration.confirm")
public class RegistrationProperties {

    private boolean need;
    private String url;
}
