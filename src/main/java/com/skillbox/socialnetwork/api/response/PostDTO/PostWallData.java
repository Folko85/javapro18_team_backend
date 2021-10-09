package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.CommentWallData;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import lombok.Data;

import java.util.List;

@Data
public class PostWallData {
    private int id;
    private long time;
    private AuthData author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private int likes;
    private List<CommentWallData> comments;
    private String type;
    @JsonProperty("my_like")
    private boolean myLike;
    private List<String> tags;
}
