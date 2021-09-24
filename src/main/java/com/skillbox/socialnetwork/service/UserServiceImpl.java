package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.time.ZoneOffset.UTC;

@Service
public class UserServiceImpl {

   private AccountRepository accountRepository;

   public UserServiceImpl(AccountRepository accountRepository){
       this.accountRepository=accountRepository;
   }

    public UserRest getUserByEmail(String email){
        UserRest userRest = new UserRest();
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        BeanUtils.copyProperties(person,userRest );
        userRest.setDateAndTimeOfRegistration(person.getDateAndTimeOfRegistration().toEpochSecond(UTC));
        userRest.setLastOnlineTime(person.getLastOnlineTime().toEpochSecond(UTC));
        System.out.println(person.getLastOnlineTime());
        System.out.println("#############");
        System.out.println(userRest);
        return userRest;
    }
}
