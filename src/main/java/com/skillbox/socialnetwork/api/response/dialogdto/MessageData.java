package com.skillbox.socialnetwork.api.response.dialogdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)

public class MessageData implements Dto {
    private int id;
    private Instant time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private String readStatus;
    private boolean isSendByMe;

}
