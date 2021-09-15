package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.eMail = ?1 ")
    Optional<Person> findByEMail(String email);
}
