package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Сообщение.
 */
@Data
public class MessageRequest {
    @JsonProperty("message_text")
    private String messageText;
}
