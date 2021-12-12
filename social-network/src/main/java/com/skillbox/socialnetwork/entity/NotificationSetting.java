package com.skillbox.socialnetwork.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "notification_setting")
@Getter
@Setter
@Accessors(chain = true)
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @OneToOne
    private Person person;

    private boolean isPostComment;

    private boolean isCommentComment;

    private boolean isFriendsRequest;

}
