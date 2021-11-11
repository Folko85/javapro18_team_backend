package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsDto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final FriendshipRepository friendshipRepository;

    public PersonService(PersonRepository personRepository, FriendshipRepository friendshipRepository) {
        this.personRepository = personRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public FriendsDto friendsToPojo(Person source) {
        FriendsDto result = new FriendsDto();

        result.setId(source.getId());
        result.setFirstName(source.getFirstName());
        result.setLastName(source.getLastName());
        result.setRegDate(source.getDateAndTimeOfRegistration());
        result.setBirthDate(source.getBirthday());
        result.setEmail(source.getEMail());
        result.setPhone(source.getPhone());
        result.setPhoto(source.getPhoto());
        result.setAbout(source.getAbout());
        result.setCity("Город");
        result.setCountry("Страна");
        result.setMessagesPermission(source.getMessagesPermission());
        result.setLastOnlineTime(source.getLastOnlineTime());
        result.setBlocked(source.isBlocked());

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Person> findPersonById(Integer id) {
        return personRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public ListResponse<FriendsDto> searchPerson(String firstName, String lastName, int ageFrom, int ageTo, int countryId,
                                     int cityId, int offset, int itemPerPage, Principal principal) {

        log.debug("поиск пользователя");
        String emailPerson = principal.getName();
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> personPage;
        Person person = personRepository.findByEMail(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(principal.getName()));
        List<Integer> blockers =friendshipRepository.findBlockersIds(person.getId());
        blockers.add(person.getId());
        LocalDate from = LocalDate.now().minusYears(ageTo);
        LocalDate to = LocalDate.now().minusYears(ageFrom);
        if(ageFrom == ageTo && ageFrom == -1){
            from=null;
            to=null;
        }
        else if (ageFrom==-1){
            to = LocalDate.now();
        }
        else if(ageTo ==  -1){
            from = LocalDate.parse("1900-01-01");
        }
        personPage = personRepository.findByOptionalParametrs(firstName,
                lastName, from, to, "", "", pageable, blockers);



        /**
         * 1. по дате рождения (имя и фамилия не указаны)
         * 2. по имени (фамилия и дата рождения не указаны)
         * 3. по имени и дате рождения (фамилия не указана)
         * 4. по имени и фамилии (дата рождения не указана)
         * 5. по имени, фамилии и дате рождения
         */
        /**
        if (firstName.isEmpty() && lastName.isEmpty() && ageFrom >= 1 && ageTo >= 1) {
            log.debug("поиск пользователя по дате рождения");
            personPage = personRepository
                    .findPersonByBirthday(emailPerson, from, to, pageable);

        } else if (!firstName.isEmpty() && lastName.isEmpty() && ageFrom < 1 && ageTo < 1) {
            log.debug("поиск пользователя по имени");
            personPage = personRepository
                    .findPersonByFirstName(emailPerson, firstName, pageable);

        } else if (!firstName.isEmpty() && lastName.isEmpty() && ageFrom >= 1 && ageTo >= 1) {
            log.debug("поиск пользователя по имени и дате рождения");
            personPage = personRepository
                    .findPersonByFirstNameAndBirthday(emailPerson, firstName, from, to, pageable);

        } else if (!firstName.isEmpty() && !lastName.isEmpty() && ageFrom < 1 && ageTo < 1) {
            log.debug("поиск пользователя по имени и фамилии");
            personPage = personRepository
                    .findPersonByFirstNameAndLastName(emailPerson, firstName, lastName, pageable);

        } else if (!firstName.isEmpty() && !lastName.isEmpty() && ageFrom >= 1 && ageTo >= 1) {
            log.debug("поиск пользователя по имени, фамилии и дате рождения");
            personPage = personRepository
                    .findPersonByFirstNameAndLastNameAndBirthday(emailPerson, firstName, lastName, from, to, pageable);
        } else {
            log.debug("поиск пользователя без параметров");
            personPage = personRepository.findAllPerson(pageable);
        }
        **/

        return getPersonResponse(offset, itemPerPage, personPage);

    }

    @Transactional(readOnly = true)
    public Person findPersonByEmail(String eMail) {
        return personRepository
                .findByEMail(eMail).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private ListResponse<FriendsDto> getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        List<FriendsDto> persons = pageablePersonList.stream().map(this::friendsToPojo).collect(Collectors.toList());

        ListResponse<FriendsDto> postResponse = new ListResponse<>();

        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(persons);

        return postResponse;
    }
}
