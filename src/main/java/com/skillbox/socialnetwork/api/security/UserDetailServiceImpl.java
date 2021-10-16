package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    public UserDetailServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = accountRepository.findByEMail(eMail);
        if (optionalPerson.isPresent() && optionalPerson.get().isApproved()) {
            Person person = optionalPerson.get();
            person.setLastOnlineTime(LocalDateTime.now());
            accountRepository.save(person);
            return SecurityUser.fromUser(person);
        } else return null;
    }
}
