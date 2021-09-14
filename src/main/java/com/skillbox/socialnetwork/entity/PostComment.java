package com.skillbox.socialnetwork.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Post_comment")
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PostComment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
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
}
