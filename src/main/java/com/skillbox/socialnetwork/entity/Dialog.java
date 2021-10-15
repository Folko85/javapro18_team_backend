package com.skillbox.socialnetwork.entity;

import lombok.Data;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "dialog")
@Data
public class Dialog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    private String title;

    private boolean isDialog;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dialog")
    private Set<Message> messages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person2dialog",
            joinColumns = {@JoinColumn(name = "dialog_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private Set<Person> persons;
}
