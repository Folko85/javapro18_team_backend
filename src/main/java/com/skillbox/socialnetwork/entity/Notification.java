package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.Contact;
import com.skillbox.socialnetwork.entity.enums.EntityId;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Notification {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "enum")
    private NotificationType type;

    @Column(name = "sent_time")
    private LocalDateTime sentTime;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_id", columnDefinition = "enum")
    private EntityId entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact", columnDefinition = "enum")
    private Contact contact;


}
