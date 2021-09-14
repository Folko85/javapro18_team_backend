package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.ActionType;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "Block_history")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class BlockHistory {

    @Id
    @Column
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 50, columnDefinition = "enum")
    private ActionType action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlockHistory that = (BlockHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
