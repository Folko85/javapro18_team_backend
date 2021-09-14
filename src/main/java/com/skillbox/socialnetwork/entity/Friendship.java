package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table (name = "Friendship")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Friendship {

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne
    @JoinColumn (columnDefinition = "status_id")
    private FriendshipStatus status;

    @ManyToOne
    @JoinColumn (columnDefinition = "src_person_id")
    private Person srcPerson;

    @ManyToOne
    @JoinColumn (columnDefinition = "dst_person_id")
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
