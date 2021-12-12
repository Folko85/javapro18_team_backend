package com.skillbox.socialnetwork.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
    private Instant datetime;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Person person;

    @Column(columnDefinition = "mediumtext")
    private String title;

    @Column(name = "post_text", columnDefinition = "longtext")
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    private boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedTimestamp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private Set<PostComment> comments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "post2tag",
            joinColumns = {@JoinColumn(name = "post_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})

    private Set<Tag> tags;

}
