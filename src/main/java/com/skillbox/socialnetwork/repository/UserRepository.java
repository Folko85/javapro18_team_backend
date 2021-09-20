package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.eMail = ?1 ")
    Optional<User> findByEMail(String email);
}
