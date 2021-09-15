package com.skillbox.socialnetwork.persistenceContext;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Post_file")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class PostFile {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String name;

    @Column
    private String path;
}
