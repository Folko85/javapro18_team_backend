package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.authdto.Place;
import com.skillbox.socialnetwork.api.response.authdto.UserRest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    private PersonRepository personRepository;

    public UserServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public UserRest getUserByEmail(String email) {
        Person person = personRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(person, userRest);
        Place city = new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;
    }

    public UserRest getUserById(Integer id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("" + id));
        UserRest userRest = new UserRest();
        BeanUtils.copyProperties(person, userRest);
        Place city = new Place();
        city.setId(1);
        city.setTitle(person.getTown());
        userRest.setCity(city);
        return userRest;

    }

    public UserRest updateUser(UserRest updates) {
        Person person = personRepository.findByEMail(updates.getEMail())
                .orElseThrow(() -> new UsernameNotFoundException("" + updates.getEMail()));
        person.setFirstName(updates.getFirstName());
        person.setLastName(updates.getLastName());
        person.setBirthday(updates.getBirthday());
        person.setPhone(updates.getPhone());
        person.setAbout(updates.getAbout());
        person.setMessagesPermission(updates.getMessagesPermission());
        Person updatedPerson = personRepository.save(person);
        UserRest updated = new UserRest();
        BeanUtils.copyProperties(updatedPerson, updated);
        return updated;
    }

    public void deleteUser(String email) {
        Person person = personRepository.findByEMail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        personRepository.delete(person);
    }


}
