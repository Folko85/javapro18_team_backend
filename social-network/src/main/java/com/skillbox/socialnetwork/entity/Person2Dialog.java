package com.skillbox.socialnetwork.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Связь человека с диалогом.
 */
@Entity
@Table(name = "person2dialog")
@Data
@RequiredArgsConstructor
@Accessors(chain = true)
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
