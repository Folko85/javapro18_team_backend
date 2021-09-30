package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.response.FriendsDTO.*;
import com.skillbox.socialnetwork.entity.Person;
import org.springframework.stereotype.Service;

@Service
public class MappingUtils {

    public static FriendsDTO friendsToPojo(Person source) {
        FriendsDTO result = new FriendsDTO();

        result.setId(source.getId());
        result.setFirstName(source.getFirstName());
        result.setLastName(source.getLastName());
        result.setRegDate(source.getDateAndTimeOfRegistration());
        result.setBirthDate(source.getBirthday());
        result.setEMail(source.getEMail());
        result.setPhone(source.getPhone());
        result.setPhoto(source.getPhoto());
        result.setAbout(source.getAbout());
        result.setCity("Город");
        result.setCountry("Страна");
        result.setMessagesPermission(source.getMessagesPermission());
        result.setLastOnlineTime(source.getLastOnlineTime());
        result.setBlocked(source.isBlocked());

        return result;
    }

}
