package com.skillbox.socialnetwork.api.response.friendsdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendResponse {


    private String error;


    private LocalDateTime timestamp;


    private String message;


}
