package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.Contact;
import com.skillbox.socialnetwork.entity.enums.EntityId;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification")
@Getter
@Setter
public class Notification {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "enum")
    private NotificationType type;

    @Column(name = "send_time")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id && type == that.type && Objects.equals(sentTime, that.sentTime) && Objects.equals(person, that.person) && entityId == that.entityId && contact == that.contact;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, sentTime, person, entityId, contact);
    }
}
