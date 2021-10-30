package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    public UserDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        Person person = personRepository.findByEMail(eMail).orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
        if (!person.isApproved() || person.isDeleted()) {
            throw new LockedException("Учётная запись не действительна");
        } else {
            person.setLastOnlineTime(LocalDateTime.now());
            return personRepository.save(person);
        }
    }
}
