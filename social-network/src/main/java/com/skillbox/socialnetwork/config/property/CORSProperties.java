package com.skillbox.socialnetwork.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Set;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "cors")
public class CORSProperties {

    Set<String> urls;
}
