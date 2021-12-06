package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;

@Data
public class NotificationsRequest {
    @JsonProperty("notification_type")
    private NotificationType notificationType;
    private boolean enable;
}
