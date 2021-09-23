package com.skillbox.socialnetwork.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "post2tag")
public class Post2Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    @OneToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}