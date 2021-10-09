package com.skillbox.socialnetwork.api.response.postdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.commentdto.CommentData;
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
    private List<Dto> comments;
    @JsonProperty("my_like")
    private boolean myLike;
}
