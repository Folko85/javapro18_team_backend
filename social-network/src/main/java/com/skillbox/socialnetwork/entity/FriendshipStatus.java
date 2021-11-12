package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "friendship_status")
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FriendshipStatus that = (FriendshipStatus) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
