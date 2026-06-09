package com.skillbox.socialnetwork.api.response.dialogdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Данные диалога.
 */
@Data
@Accessors(chain = true)
public class DialogData implements Dto {
    private int id;
    @JsonProperty("unread_count")
    private long unreadCount;
    @JsonProperty("last_message")
    private MessageData lastMessage;
    @JsonProperty("recipient_id")
    private AuthData recipientId;
}
