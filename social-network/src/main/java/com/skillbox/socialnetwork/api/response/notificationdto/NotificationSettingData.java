package com.skillbox.socialnetwork.api.response.notificationdto;

import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;

import java.util.List;
@Data
public class NotificationSettingData implements Dto {
    private List<NotificationType> enable;
}
