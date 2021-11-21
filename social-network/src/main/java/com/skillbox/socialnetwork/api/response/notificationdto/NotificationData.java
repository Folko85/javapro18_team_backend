package com.skillbox.socialnetwork.api.response.notificationdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class NotificationData implements Dto {
    private int id;
    @JsonProperty("event_type")
    private NotificationType eventType;
    @JsonProperty("sent_time")
    private Instant sentTime;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("parent_entity_id")
    private Integer parentEntityId;
    @JsonProperty("entity_author")
    private AuthData entityAuthor;
    @JsonProperty("current_entity_id")
    private int currentEntityId;
}
