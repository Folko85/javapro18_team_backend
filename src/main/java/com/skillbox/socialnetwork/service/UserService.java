package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
public class UserService {

    public static String deletedImage="http://res.cloudinary.com/mmm-skillbox/image/upload/c_fill,h_300,w_300/NkbgPAkUQT";

    private final AccountRepository accountRepository;
    private final FriendshipService friendshipService;

    public UserService(AccountRepository accountRepository, FriendshipService friendshipService) {
        this.accountRepository = accountRepository;
        this.friendshipService = friendshipService;
    }

    public AuthData getUserByEmail(Principal principal) {
        AuthData userRest = new AuthData();
        convertUserToUserRest(getPersonByEmail(principal), userRest);
        log.info("User with email "+userRest.getEMail()+" was received");
        return userRest;
    }

    public Person getPersonByEmail(Principal principal){
        return accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> {
                    log.error("Get User By Email Failed in UserService Class, email: "+principal.getName());
                    return new UsernameNotFoundException("Email was not found");
                });
    }

    public AuthData getUserById(Integer id) {
        AuthData userRest = new AuthData();
        convertUserToUserRest(getPersonById(id), userRest);
        log.info("User with id "+userRest.getId()+" was received");
        return userRest;
    }

    public Person getPersonById(Integer id){
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Get User By Id Failed in UserService Class, id: "+id);
                    return new UsernameNotFoundException("Id was not found " + id);
                });
    }

    public DataResponse getUserMe(Principal principal) {
        return createResponse(getUserByEmail(principal));
    }

    public DataResponse createResponse(AuthData authData, String error) {
        DataResponse userRestResponse = new DataResponse();
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setData(authData);
        userRestResponse.setError(error);
        return userRestResponse;
    }

    public DataResponse createResponse(AuthData authData) {
        return createResponse(authData, "null");
    }

    public DataResponse getUser(int id, Principal principal) {
        AuthData current = getUserByEmail(principal);
        log.info("Attempt to get user by Id, requester id: " +current.getId() + " target id: "+id);
        AuthData requested = getUserById(id);
        if (friendshipService.isBlockedBy(requested.getId(), current.getId()) && !requested.isDeleted()) {
            AuthData response = new AuthData();
            response.setId(requested.getId());
            response.setFirstName(requested.getFirstName());
            response.setLastName(requested.getLastName());
            response.setAbout("Отдыхай в чс, пыль");
            log.warn("Requester id: " +current.getId() + "was blocked by "+ id);
            return createResponse(response, "BLOCKED");
        } else {
            log.info("Attempt to get user by id" +id +"is successful");
            return createResponse(requested);
        }

    }

    public DataResponse updateUser(AuthData updates, Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> {
                    log.error("Update User Failed. Email was not found, email: "+principal.getName());
                    return new UsernameNotFoundException("Update User Failed");
                });
        log.info("Attempt to update user, id: " + person.getId());
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
        log.info("User "+person.getId()+" was updated");
        return response;
    }

    public AccountResponse deleteUser(Principal principal) {
        Person person = accountRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        person.setDeleted(true);
        accountRepository.save(person);
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
        userRest.setBirthDate(person.getBirthday() == null ? null : person.getBirthday().atStartOfDay().toInstant(UTC));
        userRest.setLastOnlineTime(person.getLastOnlineTime().toInstant(UTC));
        userRest.setRegDate(person.getDateAndTimeOfRegistration().toInstant(UTC));
        if (person.getBirthday() != null) {
            userRest.setBirthDate(person.getBirthday().atStartOfDay().toInstant(UTC));
        }
    }

    public static void convertUserToUserRest(Person person, AuthData userRest) {
        if(person.isDeleted()){
            log.info("Getting a deleted account id: " +person.getId());
            userRest.setId(person.getId());
            userRest.setFirstName(person.getFirstName());
            userRest.setLastName(person.getLastName());
            userRest.setAbout("Страница удалена");
            userRest.setPhoto(deletedImage);
            userRest.setDeleted(true);
        }
        else {
            BeanUtils.copyProperties(person, userRest);
            userRest.setBirthDate(person.getBirthday() == null ? Instant.now() : person.getBirthday().atStartOfDay().toInstant(UTC));
            conventionsFromPersonTimesToUserRest(person, userRest);
        }
    }
}
