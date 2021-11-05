package com.skillbox.socialnetwork.api.response.dialogdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)

public class MessageData implements Dto {
    private int id;
    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("message_text")
    private String messageText;
    @JsonProperty("read_status")
    private String readStatus;
    private boolean isSendByMe;

}
