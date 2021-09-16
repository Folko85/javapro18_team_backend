package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.ActionType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Block_history")
@Getter
@Setter
@EqualsAndHashCode
@ToString
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
}
