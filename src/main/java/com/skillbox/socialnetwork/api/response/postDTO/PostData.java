package com.skillbox.socialnetwork.api.response.postDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.authDTO.AuthData;
import com.skillbox.socialnetwork.api.response.CommentData;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class PostData implements Dto {
    private int id;
    private Instant time;
    private AuthData author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private int likes;
    private List<CommentData> comments;
}
