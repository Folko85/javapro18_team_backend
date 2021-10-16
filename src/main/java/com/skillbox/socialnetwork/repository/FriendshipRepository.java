package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.StatusFriend;
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

    @Query("select f from Friendship f where f.srcPerson.firstName = ?1 or f.dstPerson.firstName = ?1")
    List<Friendship> findBySrcPersonFirstNameOrDstPersonFirstName(String firstName, Pageable pageable);

    @Override
    @Query("select f from Friendship f where f.srcPerson.id = ?1 or f.dstPerson.id = ?1")
    Optional<Friendship> findById(Integer id);

    @Query("SELECT p2 " +
            "FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE p.isBlocked = false AND p.id = ?2  AND  p2.firstName LIKE  %?1% ")
    Page<Person> findPersonByFriendship(String name, int personId, Pageable pageable);

    @Query("select f " +
            "from Friendship f " +
            "where f.srcPerson.id = ?1 and f.dstPerson.id = ?2 " +
            "or f.srcPerson.id = ?2 and f.dstPerson.id = ?1")
    Optional<Friendship> findFriendshipBySrcPersonAndDstPerson(int src, int dst);

//    @Query("select " +
//            "case " +
//            "when f.srcPerson.id = ?1 and f.dstPerson.id = ?2" +
//            " then new StatusFriend(f.id, fs.code) " +
//            "end " +
//            "new com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.StatusFriend(f.id, fs.code) " +
//            "from Friendship f " +
//            "join FriendshipStatus fs on fs.id = f.id " +
//            "where f.srcPerson = ?1 and fs.code = ?2")

    @Query("select fs.code from FriendshipStatus fs " +
    "left join Friendship f on f.id = fs.id " +
    "where f.srcPerson.id = ?1 and f.dstPerson.id = ?2 " +
    "or f.srcPerson.id = ?2 and f.dstPerson.id = ?1 " +
    "and fs.code = 'FRIEND'")
    Optional<FriendshipStatusCode> isMyFriend(int idPerson, int idFriend);
}
