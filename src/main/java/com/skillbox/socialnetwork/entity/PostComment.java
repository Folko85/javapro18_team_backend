package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Post_comment")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class PostComment {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column (name = "parent_id")                    // хз, как это обыгрывать
    private Integer parentId;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Person person;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "comment")
    @ToString.Exclude
    private Set<BlockHistory> blocks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostComment that = (PostComment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
