package com.skillbox.socialnetwork.api.response.socketio;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class TypingResponse implements Dto {
    private int authorId;
    private String author;
    private int dialog;
}
