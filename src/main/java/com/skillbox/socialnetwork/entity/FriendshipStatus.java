package com.skillbox.socialnetwork.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Friendship_status")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class FriendshipStatus {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column (name = "time")
    private LocalDateTime time;

    @Column
    private String name;

    @Column
    private String code;

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
