package com.skillbox.microservice.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_of_application", nullable = false)
    private LocalDateTime dateOfApplication;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "message", nullable = false, columnDefinition = "mediumtext")
    private String message;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public String toString() {
        return id + " " + dateOfApplication + " " + client + " " + message;
    }
}
