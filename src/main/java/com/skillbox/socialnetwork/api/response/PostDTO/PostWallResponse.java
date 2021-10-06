package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostWallResponse {
    private String error;
    private long timestamp;
    private int total;
    private int offset;
    private int perPage;
    private List<PostWallData> data;
}
