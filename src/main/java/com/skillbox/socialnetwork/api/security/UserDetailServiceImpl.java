package com.skillbox.socialnetwork.api.security;

import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.User;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public UserDetailServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String eMail) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEMail(eMail);
        if(optionalUser.isEmpty()) {
            Person person = accountRepository.findByEMail(eMail)
                    .orElseThrow(() -> new UsernameNotFoundException(eMail));
            return  SecurityUser.fromUser(person);
        }
        else return SecurityUser.fromUser(optionalUser.get());
    }
}