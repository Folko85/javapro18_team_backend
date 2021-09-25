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

    @ManyToOne
    @JoinColumn (name = "person_id", nullable = false)
    private Person srcPerson;

    @ManyToOne
    @JoinColumn (name = "person_id", nullable = false)
    private Person dstPerson;

    public Person getSrcPerson() {
        return srcPerson;
    }

    public void setSrcPerson(Person srcPerson) {
        setSrcPerson(srcPerson, false);
    }

    public void setSrcPerson(Person srcPerson, boolean otherSideHasBeenSet) {
        this.srcPerson = srcPerson;
        if (otherSideHasBeenSet) {
            return;
        }
        srcPerson.addFriend(this, true);
    }

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
