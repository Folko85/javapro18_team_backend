package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.api.response.AuthDTO.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.sun.xml.bind.v2.TODO;
import liquibase.pro.packaged.U;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        convertionsFromPersonTimesToUserRest(person, userRest);
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
        convertionsFromPersonTimesToUserRest(person, userRest);
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
       person.setMessagesPermission(updates.getMessagesPermission());
       Person updatedPerson = accountRepository.save(person);
       UserRest updated= new UserRest();
       BeanUtils.copyProperties(updatedPerson, updated);
       updated.setBirthday(convertLocalDate(updatedPerson.getBirthday()));

       return updated;
    }

    public void deleteUser(String email){
        Person person = accountRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        accountRepository.delete(person);
    }


    public static long convertLocalDate(LocalDate localDate){
       java.sql.Date date = java.sql.Date.valueOf(localDate);
       return date.getTime() / 1000;

    }
    public static LocalDate covertToLocalDate(long day) {
        java.sql.Date date = new Date(day*1000);
        return date.toLocalDate();
    }
    public  static  long convertLocalDateTime(LocalDateTime localDateTime){
       return localDateTime.toEpochSecond(UTC);

   }

   public static  void convertionsFromPersonTimesToUserRest(Person person, UserRest userRest){
      userRest.setLastOnlineTime(convertLocalDateTime(person.getLastOnlineTime()));
      userRest.setDateAndTimeOfRegistration(convertLocalDateTime(person.getDateAndTimeOfRegistration()));
      userRest.setBirthday(convertLocalDate(person.getBirthday()));

   }



}
