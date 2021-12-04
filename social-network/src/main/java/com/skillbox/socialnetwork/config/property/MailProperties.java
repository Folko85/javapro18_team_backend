package com.skillbox.socialnetwork.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "external.mail")
public class MailProperties {

    private String key;
    private String secret;
    private String from;
}
