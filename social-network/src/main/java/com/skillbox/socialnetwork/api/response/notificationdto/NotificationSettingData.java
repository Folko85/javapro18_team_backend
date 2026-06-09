package com.skillbox.socialnetwork.api.response.notificationdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Настройки уведомлений.
 */
@Data
@Accessors(chain = true)
public class NotificationSettingData implements Dto {
    @JsonProperty(value = "notification_type")
    private NotificationType notificationType;
    private boolean enable;

}
