package com.skillbox.socialnetwork.api.response.CommentDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CommentData {
    @JsonProperty("parent_id")
    private Integer parentId;
    @JsonProperty("comment_text")
    private String commentText;
    private int id;
    @JsonProperty("post_id")
    private int postId;
    private Instant time;
    @JsonProperty("author_id")
    private int authorId;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    @JsonProperty("sub_comments")
    private List<CommentData> subComments;
}
