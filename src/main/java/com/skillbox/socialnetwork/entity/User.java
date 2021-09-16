package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.UserType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "e_mail", length = 50, unique = true)
    private String eMail;

    @Column(name = "password", length = 50)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, columnDefinition = "enum")
    private UserType type;

}
