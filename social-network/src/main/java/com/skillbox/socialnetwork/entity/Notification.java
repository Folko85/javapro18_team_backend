package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Сущность уведомления.
 */
@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "enum('POST','POST_COMMENT','COMMENT_COMMENT','FRIEND_REQUEST','MESSAGE')")
    private NotificationType type;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Column(name = "entity_id")
    private Integer entityId;

    @Column(name = "read_status")
    private boolean readStatus;
}
