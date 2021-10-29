package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("SELECT p FROM Person p " +
            "WHERE p.firstName = ?1 " +
            "AND p.lastName = ?2")
    List<Person> findAllByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT p FROM Person p " +
            "WHERE p.eMail = ?1")
    Optional<Person> findByEMail(String email);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id = ?1 ")
    Person findByPersonId(Integer id);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id IN ?1")
    Page<Person> findByPersonIdList(List<Integer> id, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName = ?1 ")
    Person findByName(String name);

    //    @Query("SELECT p2 " +
//            "FROM Person p " +
//            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
//            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
//            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
//            "WHERE p.isBlocked = false " +
//            "AND p.id = ?2 " +
//            "AND p2.firstName LIKE ?1% " +
//            "AND fs.code = ?3")

    @Query("SELECT f " +
            "FROM Friendship f " +
            "LEFT JOIN FriendshipStatus fs ON fs.id = f.id " +
            "WHERE (f.srcPerson.id = ?1 " +
            "OR f.dstPerson.id = ?1) " +
            "AND fs.code = ?2 ")
    List<Friendship> findPersonByFriendship(int personId, FriendshipStatusCode friendshipStatusCode, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName LIKE ?2% " +
            "AND p.eMail NOT LIKE ?1 " +
            "AND p.isBlocked = false")
    Page<Person> findPersonByFirstName(String email, String firstName, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName LIKE ?2% " +
            "AND p.birthday BETWEEN ?3 AND ?4 " +
            "AND p.eMail NOT LIKE ?1 " +
            "AND p.isBlocked = false")
    Page<Person> findPersonByFirstNameAndBirthday(String email, String firstName, LocalDate ageFrom, LocalDate ageTo, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName LIKE ?2% " +
            "AND p.lastName LIKE ?3% " +
            "AND p.eMail not like ?1 " +
            "AND p.isBlocked = false ")
    Page<Person> findPersonByFirstNameAndLastName(String email, String firstName, String lastName, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName LIKE ?2% " +
            "AND p.lastName LIKE ?3% " +
            "AND p.birthday BETWEEN ?4 AND ?5 " +
            "AND p.eMail not like ?1 " +
            "AND p.isBlocked = false ")
    Page<Person> findPersonByFirstNameAndLastNameAndBirthday(String email, String firstName, String lastName, LocalDate ageFrom, LocalDate ageTo, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.birthday BETWEEN ?2 AND ?3 " +
            "AND p.eMail NOT LIKE %?1% " +
            "AND p.isBlocked = false " +
            "AND p.isDeleted = false")
    Page<Person> findPersonByBirthday(String email, LocalDate date1, LocalDate date2, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.birthday BETWEEN ?2 AND ?3 " +
            "AND p.city LIKE ?4% " +
            "AND p.eMail NOT LIKE ?1 " +
            "AND p.isBlocked = false " +
            "AND p.isDeleted = false")
    Page<Person> findPersonByBirthdayAndCity(String email, LocalDate date1, LocalDate date2, String city, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.city = ?1 " +
            "AND p.isBlocked = false " +
            "AND p.isDeleted = false")
    Page<Person> findPersonByCity(String city, Pageable pageable);

    @Query("SELECT p FROM Person p " +
            "WHERE p.isBlocked = false")
    Page<Person> findAllPerson(Pageable pageable);

    @Query("SELECT p FROM Person p " +
            "WHERE p.isBlocked = false " +
            "AND p.isDeleted = false")
    Page<Person> find10Person(Pageable pageable);
}
