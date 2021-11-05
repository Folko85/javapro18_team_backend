package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;

    public UserDetailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eMail) {
        return personRepository.findByEMail(eMail).orElse(null);
    }
}
