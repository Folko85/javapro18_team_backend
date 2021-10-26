package com.skillbox.socialnetwork.repository;

import com.skillbox.socialnetwork.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query("select p from Person p where p.firstName = ?1 AND p.lastName = ?2")
    List<Person> findAllByFirstNameAndLastName(String firstName, String lastName);

    @Query("SELECT p FROM Person p WHERE p.eMail = ?1")
    Optional<Person> findByEMail(String email);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.id = ?1 ")
    Person findByPersonId(Integer id);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName = ?1 ")
    Person findByName(String name);

    @Query("SELECT p2 " +
            "FROM Person p " +
            "LEFT JOIN Friendship f ON f.srcPerson.id = p.id " +
            "LEFT JOIN Person p2 ON p2.id = f.dstPerson.id " +
            "WHERE p.isBlocked = false AND p.id = ?2  AND  p2.firstName LIKE  ?1% ")
    Page<Person> findPersonByFriendship(String name, int personId, Pageable pageable);


    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName LIKE ?2 and p.birthday between ?3 and ?4 and p.eMail not like ?1")
    Page<Person> findPersonByFirstNameAndBirthday(String email, String firstName, LocalDate ageFrom, LocalDate ageTo, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.firstName = ?2 and p.lastName LIKE ?3% and p.birthday between ?4 and ?5 and p.eMail not like ?1")
    Page<Person> findPersonByFirstNameAndLastNameAndBirthday(String email, String firstName, String lastName, LocalDate ageFrom, LocalDate ageTo, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.birthday between ?2 and ?3 and p.eMail not like ?1")
    Page<Person> findPersonByBirthday(String email, LocalDate date1, LocalDate date2, Pageable pageable);

    @Query("SELECT p " +
            "FROM Person p " +
            "WHERE p.birthday between ?2 and ?3 and p.city = ?4 and p.eMail not like ?1")
    Page<Person> findPersonByBirthdayAndCity(String email, LocalDate date1, LocalDate date2, String city, Pageable pageable);

    @Query("select p from Person p")
    Page<Person> findAllPerson(Pageable pageable);

    @Query(value = "select * from limit 10", nativeQuery = true)
    List<Person> find10Person();
}
