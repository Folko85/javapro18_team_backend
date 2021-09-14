package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Post_comment")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class PostComment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime time;

    @Column(name = "post_id")
    private Integer postId;

    @Column
    private Integer parent_id;

    @Column(name = "autor_id")
    private int autorId;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostComment that = (PostComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
