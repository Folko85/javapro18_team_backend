package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.CommentData;
import lombok.Data;

import java.util.List;

@Data
public class PostData {
    private int id;
    private long time;
    private AuthData author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private int likes;
    private List<CommentData> comments;
}
