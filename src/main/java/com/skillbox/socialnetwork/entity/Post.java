package com.skillbox.socialnetwork.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
    @OneToOne
    private Integer id;

    @Column
    private LocalDateTime datetime;

    @Column(name = "autor_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Person autorId;

    @Column
    private String title;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "is_blocked")
    private boolean isBlocked;
}
