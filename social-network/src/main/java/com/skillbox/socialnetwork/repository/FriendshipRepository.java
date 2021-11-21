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

    @Query("SELECT psrc FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "LEFT JOIN Person psrc ON f.srcPerson.id = psrc.id " +
            "WHERE p.firstName LIKE %:name% AND p.id = :idFriend " +
            "AND fs.code = :friendshipStatusCode AND p.isBlocked = false")
    Page<Person> findPersonByStatusCode(String name, int idFriend, FriendshipStatusCode friendshipStatusCode, Pageable pageable);


    @Query(nativeQuery = true, value =
        "SELECT perId FROM ( " +
                "SELECT f.src_person_id as perId from friendship f \n" +
                "LEFT JOIN friendship_status fs ON fs.id = f.status_id \n" +
                "WHERE \n" +
                "( f.src_person_id = ?1 AND fs.code = 'WASBLOCKEDBY' ) \n" +
                "OR ( f.dst_person_id = ?1 AND fs.code = 'BLOCKED' ) \n" +
                "OR ( f.src_person_id = ?1 OR f.src_person_id = ?1 ) AND fs.code = 'DEADLOCK' " +
                "UNION " +
                "SELECT f.dst_person_id as perId from friendship f \n" +
                "LEFT JOIN friendship_status fs ON fs.id = f.status_id \n" +
                "WHERE \n" +
                "( f.src_person_id = ?1 AND fs.code = 'WASBLOCKEDBY' ) \n" +
                "OR ( f.dst_person_id = ?1 AND fs.code = 'BLOCKED' ) \n" +
                "OR ( f.src_person_id = ?1 OR f.src_person_id = ?1 ) AND fs.code = 'DEADLOCK' "+
                " ) as per where per.perId != ?1 " )
    List<Integer> findBlockersIds(int id);

}
