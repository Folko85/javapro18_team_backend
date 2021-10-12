package com.skillbox.socialnetwork.api.response.dialogdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DialogData implements Dto {
    private int id;
    @JsonProperty("unread_count")
    public long unreadCount;
    @JsonProperty("last_message")
    private MessageData lastMessage;
}
