package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

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

    public DataResponse getUserMe(Principal principal){
        return createResponse(getUserByEmail(principal));
    }

    public  DataResponse createResponse(AuthData authData){
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setData(authData);
        userRestResponse.setError("null");
        return userRestResponse;
    }

    public DataResponse getUser(int id){
        return createResponse(getUserById(id));
    }

    public DataResponse updateUser(AuthData updates, Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Person Not Found"));
        String updatedName = updates.getFirstName().isEmpty() ? person.getFirstName() : updates.getFirstName();
        person.setFirstName(updatedName);
        String updatedLastName = updates.getLastName().isEmpty() ? person.getLastName() : updates.getLastName();
        person.setLastName(updatedLastName);
        person.setPhone(updates.getPhone());
        person.setAbout(updates.getAbout());
        person.setPhoto(updates.getPhoto());
        person.setCity(updates.getCity());
        person.setCountry(updates.getCountry());
        person.setBirthday(LocalDate.ofInstant(updates.getBirthDate(), ZoneId.systemDefault()));
        person.setMessagesPermission(updates.getMessagesPermission() == null ? person.getMessagesPermission() : updates.getMessagesPermission());
        Person updatedPerson = accountRepository.save(person);
        AuthData updated = new AuthData();
        convertUserToUserRest(updatedPerson, updated);
        DataResponse response = new DataResponse();
        response.setTimestamp(Instant.now());
        response.setData(updated);
        return response;
    }


    /**
     *{@link com.sun.xml.bind.v2.TODO}
     *Вернутся к этому методу, когда будет настроено удаление остального.
     */
    public AccountResponse deleteUser(Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        accountRepository.delete(person);
        AccountResponse userDeleteResponse = new AccountResponse();
        userDeleteResponse.setTimestamp(Instant.now());
        userDeleteResponse.setError("string");
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        userDeleteResponse.setData(dateMap);
        SecurityContextHolder.clearContext();
        return userDeleteResponse;
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
