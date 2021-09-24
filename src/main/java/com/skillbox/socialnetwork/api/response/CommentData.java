package com.skillbox.socialnetwork.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentData {
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("comment_text")
    private String commentText;
    private int id;
    @JsonProperty("post_id")
    private int postId;
    private long time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
}
