package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import liquibase.pro.packaged.U;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

import static java.time.ZoneOffset.UTC;

@Service
public class UserServiceImpl {

   private AccountRepository accountRepository;

   public UserServiceImpl(AccountRepository accountRepository){
       this.accountRepository=accountRepository;
   }

    public UserRest getUserByEmail(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(person,userRest );
        Place city= new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;
    }
    public  UserRest getUserById(Integer id){
        Person person = accountRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(""+id));
        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(person,userRest );
        Place city= new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;

    }
    public  UserRest updateUser(UserRest updates){
       Person person = accountRepository.findByEMail(updates.getEMail())
                .orElseThrow(() -> new UsernameNotFoundException(""+updates.getEMail()));
       person.setFirstName(updates.getFirstName());
       person.setLastName(updates.getLastName());
       java.sql.Date date = new Date(updates.getBirthday());
       LocalDate localDate = date.toLocalDate();
       person.setBirthday(localDate);
       person.setPhone(updates.getPhone());
       person.setAbout(updates.getAbout());
       person.setMessagesPermission(updates.getMessagesPermission());
       Person updatedPerson = accountRepository.save(person);
       UserRest updated= new UserRest();
       BeanUtils.copyProperties(updatedPerson, updated);
       return updated;
    }

    public void deleteUser(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        accountRepository.delete(person);
    }


}
