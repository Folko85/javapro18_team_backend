package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.entity.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "reg_date")
    private LocalDateTime dateAndTimeOfRegistration;

    @Column(name = "birth_date")
    private LocalDate birthday;

    @Column(name = "e_mail", length = 50, unique = true)
    private String eMail;

    @Column(name = "phone", length = 12, unique = true)
    private String phone;

    @Column(name = "password")
    private String password;

    @Column(name = "photo")
    private String photo;

    @Column(columnDefinition = "text")
    private String about;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "confirmation_code", length = 50)
    private String confirmationCode;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Enumerated(EnumType.STRING)
    @Column(name = "messages_permission", columnDefinition = "enum('ALL','FRIENDS')")
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    private boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<Post> post;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<PostComment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<BlockHistory> blockHistories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<Notification> notifications;

    public Role getRole() {
        return Role.USER;
    }
}
