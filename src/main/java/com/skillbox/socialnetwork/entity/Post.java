package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Post")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "time")
    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person person;

    @Column
    private String title;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<PostComment> comments;

    @OneToMany (cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<BlockHistory> blocks;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (name = "Post2Tag",
    joinColumns = {@JoinColumn(name = "tag_id")},
    inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Tag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
