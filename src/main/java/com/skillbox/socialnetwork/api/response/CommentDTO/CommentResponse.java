package com.skillbox.socialnetwork.api.response.CommentDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {
    private String error;
    private Instant timestamp;
    private CommentData commentData;

}
