package com.skillbox.socialnetwork.api.response.notificationdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationData implements Dto {
    private int id;
    @JsonProperty("event_type")
    private NotificationType eventType;
    @JsonProperty("sent_time")
    private Instant sentTime;
    @JsonProperty("entity_id")
    private Integer entityId;
    private String info;
    @JsonProperty("entity_author")
    private AuthData entityAuthor;
}
