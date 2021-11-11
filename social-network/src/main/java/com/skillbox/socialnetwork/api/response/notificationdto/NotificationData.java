package com.skillbox.socialnetwork.api.response.notificationdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

import java.time.Instant;

@Data
public class NotificationData implements Dto {
    private int id;
    @JsonProperty("type_id")
    private Integer typeId;
    @JsonProperty("sent_time")
    private Instant sentTime;
    @JsonProperty("entity_id")
    private Integer entityId;
    private String info;
}
