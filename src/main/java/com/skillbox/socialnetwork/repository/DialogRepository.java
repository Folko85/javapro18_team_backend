package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DialogRepository extends JpaRepository<Dialog, Integer> {
    @Query("SELECT d " +
            "FROM Dialog d " +
            "LEFT JOIN Person2Dialog p2d ON p2d.dialog.id = d.id " +
            "LEFT JOIN Person p ON p.id = p2d.person.id " +
            "WHERE (p.id = ?2  OR p.id = ?1) AND d.isDialog = true " +
            "GROUP BY d.id " +
            "HAVING COUNT(p) > 1 ")
    List<Dialog> findPerson2DialogByPersonDialog(int currentPersonId, int personId);
}
