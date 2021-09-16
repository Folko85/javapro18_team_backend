package com.skillbox.socialnetwork.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Notification_type")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class NotificationType {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", columnDefinition = "enum")
    private com.skillbox.socialnetwork.entity.enums.NotificationType code;

    @Column
    private String name;
}
