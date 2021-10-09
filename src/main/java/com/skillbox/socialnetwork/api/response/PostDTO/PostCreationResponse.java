package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCreationResponse {
    private String error;
    private long timestamp;
    private PostWallData data;
}
