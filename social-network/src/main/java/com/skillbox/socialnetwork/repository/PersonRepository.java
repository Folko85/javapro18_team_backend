package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p FROM Person p " +
            "WHERE p.eMail = ?1")
    Optional<Person> findByEMail(String email);


    @Query("SELECT p FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id OR f.srcPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE p.firstName LIKE %:name% AND p.id <> :idFriend " +
            "AND (f.dstPerson.id = :idFriend OR f.srcPerson.id = :idFriend) " +
            "AND fs.code = 'FRIEND' AND p.isBlocked = false")
    Page<Person> findFriends(String name, int idFriend, Pageable pageable);

    @Query("SELECT psrc FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "LEFT JOIN Person psrc ON f.srcPerson.id = psrc.id " +
            "WHERE p.firstName LIKE %:name% AND p.id = :idFriend " +
            "AND fs.code = :friendshipStatusCode AND p.isBlocked = false")
    Page<Person> findPersonByStatusCode(String name, int idFriend, FriendshipStatusCode friendshipStatusCode, Pageable pageable);

    @Query("SELECT p FROM Person p " +
            "WHERE p.eMail NOT LIKE :email " +
            "AND p.isBlocked = false " +
            "AND p.isDeleted = false " +
            "AND p.id NOT IN (:blockers) ")
    Page<Person> find10Person(String email, Pageable pageable, List<Integer> blockers);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE ( p.firstName LIKE :firstName||'%'  AND :firstName != ''   OR :firstName = '' )  " +
            "AND ( p.lastName LIKE :lastName||'%' AND :lastName != ''  OR :lastName = '' )  " +
            "AND (  p.birthday >= :ageFrom AND p.birthday <= :ageTo OR :ageFrom is NULL AND :ageTo is NULL) " +
            "AND ( p.city LIKE :city||'%' AND :city!='' OR :city='' ) " +
            "AND ( p.country LIKE :country||'%' AND :country!='' OR :country='' ) " +
            "AND p.isBlocked = false " +
            "AND p.isDeleted = false " +
            "AND p.id NOT IN (:blockers) ")
    Page<Person> findByOptionalParametrs(
            @NotNull String firstName, @NotNull String lastName, LocalDate ageFrom,
            LocalDate ageTo, @NotNull String city, @NotNull String country, Pageable pageable, List<Integer> blockers);

    @Query("SELECT p FROM Person p " +
            "WHERE p.deletedTimestamp < :minusMonths")
    List<Person> findSoftDeletedPersonsID(@Param("minusMonths") LocalDateTime minusMonths);

    @Query("SELECT p.id FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id OR f.srcPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.status.id " +
            "WHERE " +
            "( f.srcPerson.id = :idFriend  OR f.dstPerson.id = :idFriend ) AND fs.code IS NOT NULL " +
            "AND p.isBlocked = false " +
            "AND p.id!= :idFriend " +
            "ORDER BY p.id ASC ")
    List<Integer> findPersonRelastionShips(int idFriend);

    @Query("SELECT p.id FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id OR f.srcPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.status.id " +
            "WHERE ( " +
            "( f.srcPerson.id = :idFriend AND fs.code IN ('WASBLOCKEDBY', 'DEADLOCK' ) ) " +
            " OR ( f.dstPerson.id = :idFriend AND fs.code IN ('DEADLOCK', 'BLOCKED') ) " +
            ") " +
            "AND p.isBlocked = false " +
            "AND p.id!= :idFriend " +
            "ORDER BY p.id ASC  ")
    List<Integer> findBlockersIds(int idFriend);

    @Query("SELECT p.id FROM Person p " +
            "LEFT JOIN Friendship f ON f.dstPerson.id = p.id OR f.srcPerson.id = p.id " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.status.id " +
            "WHERE " +
            "( f.srcPerson.id = :id OR  f.dstPerson.id = :id ) AND fs.code='FRIEND'  " +
            "AND p.isBlocked = false " +
            "AND p.id!= :id " +
            "ORDER BY p.id ASC  ")
    List<Integer> findFriendsIds(int id);

    @Query(nativeQuery = true, value =
            "WITH " +
                    "t1 AS ( SELECT p.id as pid FROM person p " +
                    "LEFT JOIN friendship f ON f.src_person_id = p.id OR f.dst_person_id =p.id " +
                    "LEFT JOIN friendship_status fs  on fs.id = f.status_id " +
                    "WHERE  (f.src_person_id = :id or f.dst_person_id= :id) AND fs.code ='FRIEND' " +
                    "AND p.id != :id " +
                    "AND p.is_blocked =0 " +
                    ") " +
            "SELECT p2.id from person p2 " +
            "LEFT JOIN friendship f ON f.src_person_id = p2.id OR f.dst_person_id =p2.id " +
            "LEFT JOIN friendship_status fs  on fs.id = f.status_id " +
            "WHERE (f.src_person_id IN (SELECT pid from t1)  OR f.dst_person_id IN (SELECT pid from t1) ) " +
            "AND fs.code ='FRIEND' " +
            "AND p2.id NOT IN ((SELECT pid from t1)) " +
            "AND p2.id !=:id " + " " +
            "ORDER BY p2.id ASC ")
    List<Integer> findFriendsOfFriendsIds(int id);


    @Query(nativeQuery = true, value =
            "WITH " +
                    "t1 AS ( SELECT p.id as pid FROM person p " +
                    "LEFT JOIN friendship f ON f.src_person_id = p.id OR f.dst_person_id =p.id " +
                    "LEFT JOIN friendship_status fs  on fs.id = f.status_id " +
                    "WHERE  (f.src_person_id = :id or f.dst_person_id= :id) AND fs.code ='FRIEND' " +
                    "AND p.id != :id " +
                    "AND p.is_blocked =0 " +
                    ") " +
            "SELECT p2.id from person p2 " +
            "LEFT JOIN friendship f ON f.src_person_id = p2.id OR f.dst_person_id =p2.id " +
            "LEFT JOIN friendship_status fs  on fs.id = f.status_id " +
            "WHERE (f.src_person_id IN (SELECT pid from t1)  OR f.dst_person_id IN (SELECT pid from t1) ) " +
            "AND fs.code ='FRIEND' " +
            "AND p2.id !=:id " +
            "UNION " +
            "SELECT p3.id as id from person p3 " +
            "LEFT JOIN friendship f ON  f.dst_person_id =p3.id " +
            "LEFT JOIN friendship_status fs  on fs.id = f.status_id " +
            "WHERE  f.src_person_id = :id  AND  ( fs.code ='SUBSCRIBED' OR fs.code ='REQUEST' ) " +
            "AND p3.id !=:id " +
            "AND p3.is_blocked =0 " +
            "ORDER BY id ASC "
    )
    List<Integer> findFriendsAndFriendsOfFriendsAndSubscribesIds(int id);

}
