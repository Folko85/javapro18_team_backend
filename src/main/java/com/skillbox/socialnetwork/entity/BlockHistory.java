package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.ActionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "block_history")
@Getter
@Setter
public class BlockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 50, columnDefinition = "enum")
    private ActionType action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockHistory that = (BlockHistory) o;
        return Objects.equals(id, that.id) && Objects.equals(time, that.time) && Objects.equals(person, that.person) && Objects.equals(post, that.post) && Objects.equals(postComment, that.postComment) && action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, person, post, postComment, action);
    }
}
