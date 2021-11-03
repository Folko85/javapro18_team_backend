package com.skillbox.socialnetwork.api.response.commentdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
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
    private Integer authorId;
    @JsonProperty("is_blocked")
    private boolean isBlocked;
    @JsonProperty("is_deleted")
    private boolean isDeleted;
    @JsonProperty("sub_comments")
    private List<CommentData> subComments;
    private int likes;
    @JsonProperty("my_like")
    private boolean myLike;
}
