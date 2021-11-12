package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.NotificationType;
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
