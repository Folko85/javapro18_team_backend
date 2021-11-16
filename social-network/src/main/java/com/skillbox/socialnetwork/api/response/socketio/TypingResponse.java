package com.skillbox.socialnetwork.api.response.socketio;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

@Data
public class TypingResponse implements Dto {
    private int authorId;
    private String author;
    private int dialog;
}
