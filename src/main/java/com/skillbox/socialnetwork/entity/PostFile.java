package com.skillbox.socialnetwork.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "post_file")
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PostFile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String name;

    @Column
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PostFile postFile = (PostFile) o;
        return Objects.equals(id, postFile.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
