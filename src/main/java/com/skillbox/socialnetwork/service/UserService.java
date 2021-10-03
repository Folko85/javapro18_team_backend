package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.authDTO.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PersonRepository personRepository;

    public UserService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public UserRest getUserByEmail(String email) {
        UserRest userRest = new UserRest();
        Person person = personRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        BeanUtils.copyProperties(person, userRest);
        return userRest;
    }

    public UserRest updateUserByEmail(String email, UserRest userUpdated) {
        Person person = personRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        BeanUtils.copyProperties(userUpdated, person);
        Person updatedPerson = personRepository.save(person);
        BeanUtils.copyProperties(updatedPerson, userUpdated);
        return userUpdated;
    }
}
