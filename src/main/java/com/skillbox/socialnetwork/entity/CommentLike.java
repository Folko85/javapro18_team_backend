package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "comment_like")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    private Person person;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment postComment;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CommentLike commentLike = (CommentLike) o;
        return Objects.equals(id, commentLike.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
