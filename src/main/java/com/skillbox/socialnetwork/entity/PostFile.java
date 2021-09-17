package com.skillbox.socialnetwork.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "post_file")
@Getter
@Setter
public class PostFile {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
        if (o == null || getClass() != o.getClass()) return false;
        PostFile postFile = (PostFile) o;
        return id == postFile.id && Objects.equals(post, postFile.post) && Objects.equals(name, postFile.name) && Objects.equals(path, postFile.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, post, name, path);
    }
}
