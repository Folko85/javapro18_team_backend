package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Like;
import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface LikeRepository extends JpaRepository<Like, Integer> {

    Optional<Like> findLikeByItemAndTypeAndPerson(int item, String type, Person person);

    Set<Like> findLikesByItemAndType(int item, String type);
}
