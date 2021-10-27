package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    public UserDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByEMail(eMail);
        if (optionalPerson.isPresent() && optionalPerson.get().isApproved() && !optionalPerson.get().isDeleted()) {
            Person person = optionalPerson.get();
            person.setLastOnlineTime(LocalDateTime.now());
            personRepository.save(person);
            return SecurityUser.fromUser(person);
        } else return null;
    }
}
