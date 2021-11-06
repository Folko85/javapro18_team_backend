package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "notification_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationType {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", columnDefinition = "enum('POST','POST_COMMENT','COMMENT_COMMENT','FRIEND_REQUEST','MESSAGE')")
    private com.skillbox.socialnetwork.entity.enums.NotificationType code;

    @Column
    private String name;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "type")
    private Set<Notification> notifications;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationType that = (NotificationType) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
