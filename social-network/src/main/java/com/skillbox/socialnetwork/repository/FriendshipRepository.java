package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Integer> {

    @Query("SELECT f FROM Friendship f " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE f.srcPerson.id = :src AND f.dstPerson.id = :dst " +
            "OR f.srcPerson.id = :dst AND f.dstPerson.id = :src ")
    Optional<Friendship> findFriendshipBySrcPersonAndDstPerson(int src, int dst);

    @Query("SELECT f FROM Friendship f " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE (f.srcPerson.id = :src AND f.dstPerson.id = :dst " +
            "OR f.srcPerson.id = :dst AND f.dstPerson.id = :src) AND fs.code = 'FRIEND' ")
    Optional<Friendship> findFriendBySrcPersonAndDstPerson(int src, int dst);

    @Query("SELECT f FROM Friendship f " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE (f.srcPerson.id = :src AND f.dstPerson.id = :dst " +
            "OR f.srcPerson.id = :dst AND f.dstPerson.id = :src) ")
    Optional<Friendship> findRequestFriendship(int src, int dst);

    @Query("SELECT fs.code FROM FriendshipStatus fs " +
            "LEFT JOIN Friendship f ON f.id = fs.id " +
            "WHERE (f.srcPerson.id = ?1 AND f.dstPerson.id = ?2 " +
            "OR f.srcPerson.id = ?2 AND f.dstPerson.id = ?1) " +
            "AND fs.code = ?3 ")
    Optional<FriendshipStatusCode> isMyFriend(int idPerson, int idFriend, FriendshipStatusCode friendshipStatusCode);

}
