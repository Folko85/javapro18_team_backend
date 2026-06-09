package com.skillbox.socialnetwork.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Файл поста.
 */
@Entity
@Table(name = "post_file")
@Accessors(chain = true)
@Data
@AllArgsConstructor
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
}
