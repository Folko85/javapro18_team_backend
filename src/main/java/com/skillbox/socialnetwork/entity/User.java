package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.entity.enums.UserType;
import lombok.*;
import org.hibernate.Hibernate;

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
    private int id;

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
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public Role getRole()
    {
        return getType() == UserType.MODERATOR? Role.MODERATOR:Role.ADMIN;
    }
}
