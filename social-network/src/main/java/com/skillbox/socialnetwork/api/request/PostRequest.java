package com.skillbox.socialnetwork.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Пост.
 */
@Data
public class PostRequest {
    private String title;
    @JsonProperty("post_text")
    private String postText;
    private List<String> tags;
}
