package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsDto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

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
    public ListResponse searchPerson(String firstName, String lastName, int ageFrom, int ageTo, int countryId,
                                     int cityId, int offset, int itemPerPage, Principal principal) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> personPage = null;
        LocalDate from = LocalDate.now().minusYears(ageTo);
        LocalDate to = LocalDate.now().minusYears(ageFrom);

        if (firstName.isEmpty() && lastName.isEmpty()) {
            personPage = personRepository
                    .findPersonByBirthday(principal.getName(), from, to, pageable);
        } else if (!firstName.isEmpty() && lastName.isEmpty()) {
            personPage = personRepository
                    .findPersonByFirstNameAndBirthday(principal.getName(), firstName, from, to, pageable);
        } else if (!firstName.isEmpty() && !lastName.isEmpty()) {
            personPage = personRepository
                    .findPersonByFirstNameAndLastNameAndBirthday(principal.getName(), firstName, lastName, from, to, pageable);
        } else {
            return null;
        }

        return getPersonResponse(offset, itemPerPage, personPage);

    }

    @Transactional(readOnly = true)
    public Person findPersonByEmail(String eMail) {
        return personRepository
                .findByEMail(eMail).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    private ListResponse getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        List<Dto> persons = pageablePersonList.stream().map(this::friendsToPojo).collect(Collectors.toList());

        ListResponse postResponse = new ListResponse();

        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(persons);

        return postResponse;
    }
}
