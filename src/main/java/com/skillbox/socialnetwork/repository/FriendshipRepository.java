package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Persistence;
import java.util.Set;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE f.dstPerson.id = ?1 AND f.srcPerson = ?2")
    Person findMyFriendById(int id, Person me);

    @Query("SELECT f " +
            "FROM Friendship f " +
            "WHERE f.dstPerson.firstName = ?1 AND f.srcPerson = ?2")
    Set<Person> findMyFriendByName(String name, Person me);

}
