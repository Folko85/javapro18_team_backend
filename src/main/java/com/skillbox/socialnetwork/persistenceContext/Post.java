package com.skillbox.socialnetwork.persistenceContext;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "Post")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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

}
