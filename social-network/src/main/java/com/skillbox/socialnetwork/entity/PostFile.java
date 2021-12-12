package com.skillbox.socialnetwork.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "post_file")
@Accessors(chain = true)
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

    @Column
    private Integer userId;

    @Column
    private Integer postId;

    @Column
    private Integer commentId;

    @Column
    private String url;

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
