package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE f.dstPerson = ?1 ")
    Set<Person> findByName(Person name);
}
