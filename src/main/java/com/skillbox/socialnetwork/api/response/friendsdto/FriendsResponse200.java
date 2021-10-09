package com.skillbox.socialnetwork.api.response.friendsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendsResponse200 {
    private String error;
    private LocalDateTime timestamp;
    private String message;
}
