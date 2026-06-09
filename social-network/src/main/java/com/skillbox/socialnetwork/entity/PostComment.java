package com.skillbox.socialnetwork.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность комментарий.
 */
@Entity
@Table(name = "post_comment")
@Data
public class PostComment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    private Post post;

    @ManyToOne()
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    private PostComment parent;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @EqualsAndHashCode.Exclude
    @ToStringExclude
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
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    private Set<PostComment> postComments = new HashSet<>();
}
