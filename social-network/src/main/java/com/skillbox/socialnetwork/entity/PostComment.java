package com.skillbox.socialnetwork.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @ManyToOne()
    private PostComment parent;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person person;

    @Column(name = "comment_text", columnDefinition = "mediumtext")
    private String commentText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedTimestamp;

    @OneToMany
    @JoinColumn(name = "parent_id")
    private Set<PostComment> postComments = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostComment that = (PostComment) o;
        return Objects.equals(id, that.id);
    }
}
