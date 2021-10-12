package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person2Dialog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface DialogRepository extends JpaRepository<Person2Dialog, Integer> {

    @Query("SELECT p2d " +
            "FROM Person2Dialog p2d " +
            "LEFT JOIN Dialog d ON p2d.dialog.id = d.id " +
            "LEFT JOIN Person p ON p.id = p2d.person.id " +
            "LEFT JOIN Message m ON m.dialog.id = d.id " +
            "WHERE p.id = ?2  AND  d.title LIKE  %?1% " +
            "GROUP BY p2d.id ")
    Page<Person2Dialog> findDialogByAuthorAndTitle(String name, int personId, Pageable pageable);
}
