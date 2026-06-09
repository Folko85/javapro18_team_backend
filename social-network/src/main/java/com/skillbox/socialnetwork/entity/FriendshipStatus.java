package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Сущность статуса дружбы.
 */
@Entity
@Table(name = "friendship_status")
@Data
@Accessors(chain = true)
public class FriendshipStatus {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "time")
    private LocalDateTime time;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", columnDefinition = "enum('REQUEST', 'FRIEND', 'BLOCKED', 'DECLINED', 'SUBSCRIBED', 'WASBLOCKEDBY', 'DEADLOCK')")
    private FriendshipStatusCode code;

}
