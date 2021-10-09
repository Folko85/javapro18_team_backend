package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.time.ZoneOffset.UTC;

@Service
public class UserService {

    private AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AuthData getUserByEmail(Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        AuthData userRest = new AuthData();
        convertUserToUserRest(person, userRest);
        return userRest;
    }

    public AuthData getUserById(Integer id) {
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("" + id));
        AuthData userRest = new AuthData();
        convertUserToUserRest(person, userRest);
        return userRest;

    }

    public DataResponse updateUser(AuthData updates, Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Person Not Found"));
        updates.setId(person.getId());
        updates.setEMail(person.getEMail());
        BeanUtils.copyProperties(updates, person);
        accountRepository.save(person);
        DataResponse response = new DataResponse();
        response.setTimestamp(Instant.now());
        response.setData(updates);
        return response;
    }

    public void deleteUser(String email) {
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        accountRepository.delete(person);
    }

    public static long convertLocalDate(LocalDate localDate) {

        if (localDate == null) return 0;
        java.sql.Date date = java.sql.Date.valueOf(localDate);
        return date.getTime() / 1000;

    }

    public static long convertLocalDateTime(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return 0;
        return localDateTime.toEpochSecond(UTC);

    }

    public static LocalDateTime convertToLocalDateTime(long date) {
        if (date == 0) return null;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);
    }

    public static void conventionsFromPersonTimesToUserRest(Person person, AuthData userRest) {
        userRest.setBirthDate(person.getBirthday() == null ? null: person.getBirthday().atStartOfDay().toInstant(UTC));
        userRest.setLastOnlineTime(person.getLastOnlineTime().toInstant(UTC));
        userRest.setRegDate(person.getDateAndTimeOfRegistration().toInstant(UTC));
        if (person.getBirthday() != null){
            userRest.setBirthDate(person.getBirthday().atStartOfDay().toInstant(UTC));
        }
    }

    public static void convertUserToUserRest(Person person, AuthData userRest) {
        BeanUtils.copyProperties(person, userRest);
        userRest.setBirthDate(person.getBirthday() == null ? Instant.now() : person.getBirthday().atStartOfDay().toInstant(UTC));
        conventionsFromPersonTimesToUserRest(person, userRest);
    }
}
