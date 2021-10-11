package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findPersonByEMail(String eMail);
    @Query("SELECT p2 " +
            "FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE p.isBlocked = false AND p.id = ?2  AND  p2.firstName LIKE  %?1% ")
    Page<Person> findPersonByFriendship(String name,int personId, Pageable pageable);
}
