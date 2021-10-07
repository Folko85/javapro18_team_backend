package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.CommentDTO.CommentData;
import com.skillbox.socialnetwork.api.response.Dto;
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
    @JsonProperty("my_like")
    private boolean myLike;
}
