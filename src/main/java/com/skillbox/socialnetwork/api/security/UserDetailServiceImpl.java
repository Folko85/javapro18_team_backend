package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.User;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public UserDetailServiceImpl(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEMail(eMail);
        if(optionalUser.isEmpty()) {
            Person person = personRepository.findByEMail(eMail)
                    .orElseThrow(() -> new UsernameNotFoundException(eMail));
            person.setLastOnlineTime(LocalDateTime.now());
            personRepository.save(person);
            return  SecurityUser.fromUser(person);
        }
        else return SecurityUser.fromUser(optionalUser.get());
    }
}