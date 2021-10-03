package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.friendsDTO.FriendsDto;
import com.skillbox.socialnetwork.api.response.postDTO.Dto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
    public List<Dto> searchPerson(String firstName, String lastName, int itemPerPage) {

        Pageable pageable = Pageable.ofSize(itemPerPage);

        List<Person> personsList = personRepository
                .findAllByFirstNameAndLastName(firstName, lastName, pageable);

        if (!personsList.isEmpty()) {
            List<Dto> setPersons = personsList
                    .stream()
                    .map(this::friendsToPojo)
                    .collect(Collectors.toList());
            return setPersons;
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Person findPersonByEmail(String eMail) {
        Optional<Person> personOptional = personRepository.findByEMail(eMail);

        return personOptional.orElseGet(Person::new);
    }
}
