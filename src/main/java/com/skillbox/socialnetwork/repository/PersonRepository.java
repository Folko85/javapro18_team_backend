package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface PersonRepository extends Repository<Person, Integer> {
    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id = ?1 ")
    Person findPersonById(Integer id);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName = ?1 ")
    Person findPersonByName(String name);
}
