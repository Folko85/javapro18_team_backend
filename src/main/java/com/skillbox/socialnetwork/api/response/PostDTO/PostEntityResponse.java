package com.skillbox.socialnetwork.api.response.PostDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.CommentDTO.CommentData;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostEntityResponse {
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

    public PostEntityResponse(Post post, CommentRepository commentRepository) {
        id = post.getId();
        time = post.getDatetime();
        author = new AuthData(post.getPerson());
        title = post.getTitle();
        postText = post.getPostText();
        likes = post.getPostLikes().size();
        comments = CommentData.getCommentEntityResponseList(post.getComments(), commentRepository);
    }
}
