package com.skillbox.socialnetwork.api.response.сommentDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CommentData implements Dto {
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
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @JsonProperty("sub_comments")
    private List<CommentData> subComments;
}
