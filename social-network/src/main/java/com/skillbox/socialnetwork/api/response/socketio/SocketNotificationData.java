package com.skillbox.socialnetwork.api.response.socketio;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class SocketNotificationData implements Dto {
    private int id;
    @JsonProperty("event_type")
    private NotificationType eventType;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @JsonProperty("sent_time")
    private Instant sentTime;
    @JsonProperty("entity_id")
    private Integer entityId;
    @JsonProperty("entity_author")
    private AuthorData entityAuthor;
    @JsonProperty("parent_id")
    private int parentId;
    @JsonProperty("current_entity_id")
    private int currentEntityId;
}

