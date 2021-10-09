package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
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
    private ListResponse comments;
    @JsonProperty("my_like")
    private boolean myLike;
    private List<String> tags;

}
