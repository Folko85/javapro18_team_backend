package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id = ?1 ")
    Person findByPersonId(Integer id);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName = ?1 ")
    Person findByName(String name);
    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.eMail = ?1 ")
    Optional<Person> findPersonByEMail(String eMail);
    @Query("SELECT p2 " +
            "FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE p.isBlocked = false AND p.id = ?2  AND  p2.firstName LIKE  %?1% ")
    Page<Person> findPersonByFriendship(String name,int personId, Pageable pageable);
}
