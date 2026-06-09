package com.skillbox.socialnetwork.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Дружба.
 */
@Entity
@Table(name = "friendship")
@Data
@Accessors(chain = true)
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JoinColumn(columnDefinition = "status_id")
    @OneToOne
    private FriendshipStatus status;

    @ManyToOne
    @JoinColumn(name = "src_person_id", nullable = false)
    private Person srcPerson;

    @ManyToOne
    @JoinColumn(name = "dst_person_id", nullable = false)
    private Person dstPerson;

}
