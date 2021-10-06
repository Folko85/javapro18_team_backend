package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AccountRepository accountRepository;

    public UserService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public UserRest getUserByEmail(String email) {
        UserRest userRest = new UserRest();
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        BeanUtils.copyProperties(person, userRest);
        return userRest;
    }

    public UserRest updateUserByEmail(String email, UserRest userUpdated) {
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        BeanUtils.copyProperties(userUpdated, person);
        Person updatedPerson = accountRepository.save(person);
        BeanUtils.copyProperties(updatedPerson, userUpdated);
        return userUpdated;
    }
}
