package com.skillbox.microservice.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность сообщения.
 */
@Entity
@Data
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

}
