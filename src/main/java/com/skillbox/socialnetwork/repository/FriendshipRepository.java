package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Integer> {

    @Query("select f from Friendship f where f.srcPerson.firstName = ?1 or f.dstPerson.firstName = ?1")
    List<Friendship> findBySrcPersonFirstNameOrDstPersonFirstName(String firstName, Pageable pageable);

    @Override
    @Query("select f from Friendship f where f.srcPerson.id = ?1 or f.dstPerson.id = ?1")
    Optional<Friendship> findById(Integer id);

    @Query("select f from Friendship f where f.dstPerson.eMail = ?1 or f.srcPerson.eMail = ?1")
    List<Friendship> getAllFriends(String email);


}
