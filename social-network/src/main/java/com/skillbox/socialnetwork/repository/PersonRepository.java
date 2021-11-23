package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
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

}
