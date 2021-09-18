package com.skillbox.socialnetwork.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "post_comment")
@Getter
@Setter
public class PostComment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "parent_id")
    private Integer parentId;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Person person;

    @Column(name = "comment_text", columnDefinition = "mediumtext")
    private String commentText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostComment that = (PostComment) o;
        return isBlocked == that.isBlocked && Objects.equals(id, that.id) && Objects.equals(time, that.time) && Objects.equals(post, that.post) && Objects.equals(parentId, that.parentId) && Objects.equals(person, that.person) && Objects.equals(commentText, that.commentText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, post, parentId, person, commentText, isBlocked);
    }
}
