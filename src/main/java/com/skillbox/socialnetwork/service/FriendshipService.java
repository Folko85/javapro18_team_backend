package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.Dto;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.skillbox.socialnetwork.service.AuthService.setAuthData;
import static java.time.ZoneOffset.UTC;

@Service
public class FriendshipService {
    private final PersonRepository personRepository;


    public FriendshipService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ListResponse getFriends(String name, int offset, int itemPerPage, Principal principal) {
        Person person = findPerson(principal.getName());
        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Person> pageablePersonList = personRepository.findPersonByFriendship(name, person.getId(), pageable);
        return getPersonResponse(offset, itemPerPage, pageablePersonList);
    }

    private ListResponse getPersonResponse(int offset, int itemPerPage, Page<Person> pageablePersonList) {
        ListResponse postResponse = new ListResponse();
        postResponse.setPerPage(itemPerPage);
        postResponse.setTimestamp(LocalDateTime.now().toInstant(UTC));
        postResponse.setOffset(offset);
        postResponse.setTotal((int) pageablePersonList.getTotalElements());
        postResponse.setData(getPerson4Response(pageablePersonList.toList()));
        return postResponse;
    }

    private List<Dto> getPerson4Response(List<Person> persons) {
        List<Dto> personDataList = new ArrayList<>();
        persons.forEach(person -> {
            AuthData personData = setAuthData(person);
            personDataList.add(personData);
        });
        return personDataList;
    }

    private Person findPerson(String eMail) {
        return personRepository.findPersonByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }
}
