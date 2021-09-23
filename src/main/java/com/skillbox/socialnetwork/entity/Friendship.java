package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "friendship")
@Getter
@Setter
public class Friendship {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn (columnDefinition = "status_id")
    @OneToOne
    private FriendshipStatus status;

    @OneToOne
    @JoinColumn (name = "src_person_id")
    private Person srcPerson;

    @OneToOne
    @JoinColumn (name = "dst_person_id")
    private Person dstPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
