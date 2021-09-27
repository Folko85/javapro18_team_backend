package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.api.response.CommentData;
import com.skillbox.socialnetwork.api.response.CommentWallData;
import lombok.Data;

import java.util.List;

@Data
public class PostWallData {
    private int id;
    private long time;
    private UserRest author;
    private String title;
    @JsonProperty("post_text")
    private String postText;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    private int likes;
    private List<CommentWallData> comments;
    private String type;
}
