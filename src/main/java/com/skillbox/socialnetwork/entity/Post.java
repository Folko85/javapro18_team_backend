package com.skillbox.socialnetwork.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<PostComment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<BlockHistory> blocks;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<PostLike> likes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<PostFile> files;

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable (name = "Post2Tag",
            joinColumns = {@JoinColumn(name = "tag_id")},
            inverseJoinColumns = {@JoinColumn(name = "post_id")})
    private Set<Tag> tags;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return isBlocked == post.isBlocked && Objects.equals(id, post.id) && Objects.equals(datetime, post.datetime) && Objects.equals(person, post.person) && Objects.equals(title, post.title) && Objects.equals(postText, post.postText) && Objects.equals(comments, post.comments) && Objects.equals(blocks, post.blocks) && Objects.equals(likes, post.likes) && Objects.equals(files, post.files);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datetime, person, title, postText, isBlocked, comments, blocks, likes, files);
    }
}
