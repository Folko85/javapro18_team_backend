package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NotificationsRequest {
    @JsonProperty("notification_type")
    private String notificationType;
    private boolean enable;
}
