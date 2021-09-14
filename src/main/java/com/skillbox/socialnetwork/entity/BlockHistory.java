package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Block_history")
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BlockHistory {

    @Id
    @Column
    @PrimaryKeyJoinColumn
    private Integer id;

    @Column
    private LocalDateTime time;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "comment_id")
    private Integer commentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 50, columnDefinition = "enum")
    private ActionType action;
}
