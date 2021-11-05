package com.skillbox.socialnetwork.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "person2dialog")
@Data
public class Person2Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    private LocalDateTime addTime;

    private LocalDateTime lastCheckTime;

    @OneToOne
    private Person person;

    @OneToOne
    private Dialog dialog;

}
