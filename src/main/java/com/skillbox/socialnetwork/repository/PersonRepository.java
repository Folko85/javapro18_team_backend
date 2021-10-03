package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findById(Integer id);

    List<Person> findAllByFirstNameAndLastName(String firstName, String lastName, Pageable pageable);

    @Query("SELECT p FROM Person p WHERE p.eMail = ?1")
    Optional<Person> findByEMail(String email);

}
