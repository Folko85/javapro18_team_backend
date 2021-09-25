package com.skillbox.socialnetwork.entity;

import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.entity.enums.Role;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
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

    @Column(name = "photo", length = 255)
    private String photo;

    @Column (columnDefinition = "text")
    private String about;

    @Column(name = "town", length = 50)
    private String town;

    @Column(name = "confirmation_code", length = 50)
    private String confirmationCode;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Enumerated(EnumType.STRING)
    @Column(name = "messages_permission", length = 50, columnDefinition = "enum")
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time")
    private LocalDateTime lastOnlineTime;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<Post> post;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<PostComment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<BlockHistory> blockHistories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<PostLike> likes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
    private Set<Notification> notifications;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dstPerson")
    private Set<Friendship> friends;

    public Set<Friendship> getFriends() {
        return friends;
    }

    public void addFriend(Friendship friendship) {
        addFriend(friendship, false);
    }

    public void addFriend(Friendship friendship, boolean otherSideHasBeenSet) {
        getFriends().add(friendship);
        if (otherSideHasBeenSet) {
            return;
        }
        friendship.setSrcPerson(this, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return isApproved == person.isApproved && isBlocked == person.isBlocked && Objects.equals(id, person.id) && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(dateAndTimeOfRegistration, person.dateAndTimeOfRegistration) && Objects.equals(birthday, person.birthday) && Objects.equals(eMail, person.eMail) && Objects.equals(phone, person.phone) && Objects.equals(password, person.password) && Objects.equals(photo, person.photo) && Objects.equals(about, person.about) && Objects.equals(town, person.town) && Objects.equals(confirmationCode, person.confirmationCode) && messagesPermission == person.messagesPermission && Objects.equals(lastOnlineTime, person.lastOnlineTime) && Objects.equals(post, person.post) && Objects.equals(comments, person.comments) && Objects.equals(blockHistories, person.blockHistories) && Objects.equals(likes, person.likes) && Objects.equals(notifications, person.notifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, dateAndTimeOfRegistration, birthday, eMail, phone, password, photo, about, town, confirmationCode, isApproved, messagesPermission, lastOnlineTime, isBlocked, post, comments, blockHistories, likes, notifications);
    }

    public Role getRole()
    {
        return Role.USER;
    }
}
