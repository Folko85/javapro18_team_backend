package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.exception.ApiConnectException;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    public static String deletedImage = "http://res.cloudinary.com/mmm-skillbox/image/upload/c_fill,h_300,w_300/NkbgPAkUQT";

    private final PersonRepository personRepository;
    private final FriendshipService friendshipService;
    private final StorageService storageService;

    @Cacheable(value = "personProfileCache", key = "#principal.getName")
    public AuthData getUserByEmail(Principal principal) {
        AuthData userRest = convertUserToUserRest(getPersonByEmail(principal));
        log.info("User with email {} was received", userRest.getEMail());
        return userRest;
    }

    public Person getPersonByEmail(Principal principal) {
        return personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> {
                    log.error("Get User By Email Failed in UserService Class, email: {}", principal.getName());
                    return new UsernameNotFoundException("Email was not found");
                });
    }

    public AuthData getUserById(Integer id) {
        AuthData userRest = convertUserToUserRest(getPersonById(id));
        log.info("User with id {} was received", userRest.getId());
        return userRest;
    }

    public Person getPersonById(Integer id) {
        return personRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Get User By Id Failed in UserService Class, id: {}", id);
                    return new UsernameNotFoundException("Id was not found " + id);
                });
    }

    public DataResponse<AuthData> getUserMe(Principal principal) {
        if (principal == null) {
            throw new BadCredentialsException("Доступ запрещён");
        }
        return createResponse(getUserByEmail(principal));
    }

    public DataResponse<AuthData> createResponse(AuthData authData, String error) {
        DataResponse<AuthData> userRestResponse = new DataResponse<>();
        userRestResponse.setTimestamp(Instant.now());
        userRestResponse.setData(authData);
        userRestResponse.setError(error);
        return userRestResponse;
    }

    public DataResponse<AuthData> createResponse(AuthData authData) {
        return createResponse(authData, "null");
    }

    public DataResponse<AuthData> getUser(int id, Principal principal) {
        AuthData current = getUserByEmail(principal);
        log.info("Attempt to get user by Id, requester id: {}, target id: {}", current.getId(), id);
        AuthData requested = getUserById(id);
        if (friendshipService.isBlockedBy(requested.getId(), current.getId()) && !requested.isDeleted()) {
            AuthData response = new AuthData();
            response.setId(requested.getId());
            response.setFirstName(requested.getFirstName());
            response.setLastName(requested.getLastName());
            response.setAbout("Отдыхай в чс, пыль");
            log.warn("Requester id: {} was blocked by {}", current.getId(), id);
            return createResponse(response, "BLOCKED");
        } else {
            log.info("Attempt to get user by id {} is successful", id);
            return createResponse(requested);
        }

    }

    @CacheEvict(value = "personProfileCache", key = "#principal.getName")
    public DataResponse<AuthData> updateUser(AuthData updates, Principal principal) throws ApiConnectException {
        Person person = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> {
                    log.error("Update User Failed. Email was not found, email: {}", principal.getName());
                    return new UsernameNotFoundException("Update User Failed");
                });
        log.info("Attempt to update user, id: {}", person.getId());
        String updatedName = updates.getFirstName().isEmpty() ? person.getFirstName() : updates.getFirstName();
        person.setFirstName(updatedName);
        String updatedLastName = updates.getLastName().isEmpty() ? person.getLastName() : updates.getLastName();
        person.setLastName(updatedLastName);
        person.setPhone(updates.getPhone());
        person.setAbout(updates.getAbout());
        if (person.getPhoto() != null && person.getPhoto().contains("cloudinary") && !person.getPhoto().equals(updates.getPhoto())) {
            storageService.deleteImageByUrl(person.getPhoto());
        }
        person.setPhoto(updates.getPhoto());
        person.setCity(updates.getCity());
        person.setCountry(updates.getCountry());
        person.setBirthday(LocalDate.ofInstant(updates.getBirthDate(), ZoneId.systemDefault()));
        person.setMessagesPermission(updates.getMessagesPermission() == null ? person.getMessagesPermission() : updates.getMessagesPermission());
        Person updatedPerson = personRepository.save(person);
        AuthData updated = convertUserToUserRest(updatedPerson);
        DataResponse<AuthData> response = new DataResponse<>();
        response.setTimestamp(Instant.now());
        response.setData(updated);
        log.info("User {} was updated", person.getId());
        return response;
    }

    public DataResponse<SuccessResponse> deleteUser(Principal principal) {
        Person person = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        person.setDeleted(true);
        person.setDeletedTimestamp(LocalDateTime.now());
        personRepository.save(person);
        SecurityContextHolder.clearContext();
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));
    }

    public AuthData convertUserToUserRest(Person person) {
        AuthData userRest = new AuthData();
        if (person.isDeleted()) {
            log.info("Getting a deleted account id: {} ", person.getId());
            userRest.setId(person.getId());
            userRest.setFirstName(person.getFirstName());
            userRest.setLastName(person.getLastName());
            userRest.setAbout("Страница удалена");
            userRest.setPhoto(deletedImage);
            userRest.setDeleted(true);
        } else {
            BeanUtils.copyProperties(person, userRest);
            userRest.setBirthDate(person.getBirthday() == null ? Instant.now() : person.getBirthday().atStartOfDay().toInstant(UTC));
            userRest.setBirthDate(person.getBirthday() == null ? null : person.getBirthday().atStartOfDay().toInstant(UTC));
            userRest.setLastOnlineTime(person.getLastOnlineTime().toInstant(UTC));
            userRest.setRegDate(person.getDateAndTimeOfRegistration().toInstant(UTC));
            if (person.getBirthday() != null) {
                userRest.setBirthDate(person.getBirthday().atStartOfDay().toInstant(UTC));
            }
        }
        return userRest;
    }

    public void updateAfterSoftDelete(Person person) {
        person.setFirstName("Deleted");
        person.setLastName("Deleted");
        person.setBirthday(LocalDate.now());
        person.setPhoto(deletedImage);
        person.setDeleted(false);
        person.setAbout("Account was deleted.");
        person.setCity(null);
        person.setMessagesPermission(MessagesPermission.NOBODY);
        person.setPhone(null);
        person.setDeletedTimestamp(null);
        personRepository.save(person);
    }

    public ListResponse<AuthData> searchPerson(String firstName, String lastName, int ageFrom, int ageTo, String country,
                                               String encoded_city, int offset, int itemPerPage, Principal principal) {
        String city;
        try {
            city = URLDecoder.decode(encoded_city, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            city = "";
            log.error("Error with decoding city from URL format {}", encoded_city);
        }
        log.debug("поиск пользователя");
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> personPage;
        Person person = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        List<Integer> blockers = personRepository.findBlockersIds(person.getId());
        blockers.add(person.getId());
        LocalDate from = LocalDate.now().minusYears(ageTo);
        LocalDate to = LocalDate.now().minusYears(ageFrom);
        if (ageFrom == ageTo && ageFrom == -1) {
            from = null;
            to = null;
        } else if (ageFrom == -1) {
            to = LocalDate.now();
        } else if (ageTo == -1) {
            from = LocalDate.parse("1900-01-01");
        }
        personPage = personRepository.findByOptionalParametrs(firstName,
                lastName, from, to, city, country, pageable, blockers);

        return getPersonResponse(offset, itemPerPage, personPage);

    }

    private ListResponse<AuthData> getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        List<AuthData> persons = pageablePersonList.stream().map(this::convertUserToUserRest).collect(Collectors.toList());

        ListResponse<AuthData> postResponse = new ListResponse<>();

        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(persons);

        return postResponse;
    }
}
