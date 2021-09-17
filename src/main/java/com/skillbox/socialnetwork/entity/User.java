package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.UserType;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 50, unique = true)
    private String name;

    @Column(name = "e_mail", length = 50, unique = true)
    private String eMail;

    @Column(name = "password", length = 50)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, columnDefinition = "enum")
    private UserType type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(name, user.name) && Objects.equals(eMail, user.eMail) && Objects.equals(password, user.password) && type == user.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, eMail, password, type);
    }
}
