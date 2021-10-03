package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("select f from Friendship f where f.srcPerson.firstName = ?1 or f.dstPerson.firstName = ?1")
    List<Friendship> findBySrcPersonFirstNameOrDstPersonFirstName(String firstName, Pageable pageable);

    @Override
    @Query("select f from Friendship f where f.srcPerson.id = ?1 or f.dstPerson.id = ?1")
    Optional<Friendship> findById(Integer id);

}
