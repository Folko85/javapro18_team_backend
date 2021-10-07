package com.skillbox.socialnetwork.api.response.CommentDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.repository.CommentRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
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

    public CommentData(PostComment comment, CommentRepository repository) {
        this.parentId = comment.getParent().getId();
        this.commentText = comment.getCommentText();
        this.id = comment.getId();
        this.postId = comment.getPost().getId();
        this.time = comment.getTime().toInstant(ZoneOffset.UTC);
//        this.authorId = new AuthData(comment.getPerson()).getId();
        this.isBlocked = comment.getPost().isBlocked();
        subComments = new ArrayList<>();
        List<PostComment> subComments = repository.findByParentId(comment.getId());
        if (subComments.size() != 0 && parentId == null) addSubComment(subComments, repository);
    }

    private void addSubComment(List<PostComment> comments, CommentRepository repository) {
        comments.forEach(comment -> subComments.add(new CommentData(comment, repository)));
    }

    public static List<CommentData> getCommentEntityResponseList(List<PostComment> listComments, CommentRepository repository) {

            List<CommentData> response = new ArrayList<>();

            listComments.stream().filter(Objects::nonNull).filter(comment -> comment.getParent().getId() == null)
                    .forEach(comment -> response.add(new CommentData(comment, repository)));

            return response;
    }
}
