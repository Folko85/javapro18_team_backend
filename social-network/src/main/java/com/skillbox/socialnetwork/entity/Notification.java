package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.Contact;
import com.skillbox.socialnetwork.entity.enums.EntityId;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Notification {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name = "type_id")
    @ManyToOne
    private NotificationType type;

    @Column(name = "send_time")
    private LocalDateTime sendTime;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_id", columnDefinition = "enum('COMMENT','FRIEND','POST','MESSAGE')")
    private EntityId entityId;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact", columnDefinition = "enum('EMAIL','PHONE')")
    private Contact contact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Notification that = (Notification) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
