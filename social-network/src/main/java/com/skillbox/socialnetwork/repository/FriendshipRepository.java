package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Integer> {

    @Query("SELECT f FROM Friendship f " +
            "WHERE f.srcPerson.firstName = ?1 " +
            "OR f.dstPerson.firstName = ?1")
    List<Friendship> findBySrcPersonFirstNameOrDstPersonFirstName(String firstName, Pageable pageable);

    @Override
    @Query("SELECT f FROM Friendship f " +
            "WHERE f.srcPerson.id = ?1 " +
            "OR f.dstPerson.id = ?1")
    Optional<Friendship> findById(Integer id);

    @Query("SELECT p2 FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE p.isBlocked = false " +
            "AND p.id = ?2 " +
            "AND p2.firstName LIKE  %?1% ")
    Page<Person> findPersonByFriendship(String name, int personId, Pageable pageable);

    @Query("SELECT f FROM Friendship f " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE f.srcPerson.id = ?1 AND f.dstPerson.id = ?2 " +
            "OR f.srcPerson.id = ?2 AND f.dstPerson.id = ?1 ")
    Optional<Friendship> findFriendshipBySrcPersonAndDstPerson(int src, int dst);

    @Query("SELECT fs.code FROM FriendshipStatus fs " +
            "LEFT JOIN Friendship f ON f.id = fs.id " +
            "WHERE (f.srcPerson.id = ?1 AND f.dstPerson.id = ?2 " +
            "OR f.srcPerson.id = ?2 AND f.dstPerson.id = ?1) " +
            "AND fs.code = ?3 ")
    Optional<FriendshipStatusCode> isMyFriend(int idPerson, int idFriend, FriendshipStatusCode friendshipStatusCode);

    @Query("SELECT p2 FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE (f.srcPerson.firstName = ?1 AND f.dstPerson.id = ?2 " +
            "or f.srcPerson.id = ?2 AND f.dstPerson.firstName = ?1) " +
            "AND fs.code = ?3 AND p.isBlocked = false")
    Page<Person> findPersonByStatusCode(String name, int idFriend, FriendshipStatusCode friendshipStatusCode, Pageable pageable);

}
