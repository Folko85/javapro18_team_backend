package com.skillbox.socialnetwork.api.request.socketio;

import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

@Data
public class TypingData implements Dto {
    private int author;
    private int dialog;
}
