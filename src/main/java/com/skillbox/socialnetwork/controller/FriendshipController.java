package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.GetFriendsListRequest;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.Friends;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsList;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class FriendshipController {


    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;

    public FriendshipController(FriendshipRepository friendshipRepository, PersonRepository personRepository) {
        this.friendshipRepository = friendshipRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/api/v1/friends")
    public ResponseEntity<?> findFriend(@RequestBody GetFriendsListRequest getFriendsListRequest) {

        Person per = personRepository.findByName(getFriendsListRequest.getName());
        List<Person> personList = friendshipRepository.findByName(per);

        FriendsList response = new FriendsList();
        response.setTotal(personList.size());
        response.setTimestamp(LocalDateTime.now());

        List<Friends> friendsList = new ArrayList<>();

        if (personList.size() > 0) {
            for (Person p : personList) {

                Friends friends = new Friends();

                friends.setId(p.getId());
                friends.setFirstName(p.getFirstName());
                friends.setLastName(p.getLastName());
                friends.setRegDate(p.getDateAndTimeOfRegistration());
                friends.setBirthDate(p.getBirthday());
                friends.setEMail(p.getEMail());
                friends.setPhone(p.getPhone());
                friends.setPhoto(p.getPhoto());
                friends.setAbout(p.getAbout());
                friends.setCity("Город");
                friends.setCountry("Страна");
                friends.setMessagesPermission(p.getMessagesPermission());
                friends.setLastOnlineTime(p.getLastOnlineTime());
                friends.setBlocked(p.isBlocked());

                friendsList.add(friends);
            }

            return new ResponseEntity<>(personList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/api/v1/friends/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {

        if (friendshipRepository.existsById(id)) {
            friendshipRepository.deleteById(id);

            FriendResponse response = new FriendResponse();

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/api/v1/friends/{id}")
    public ResponseEntity<?> add(@PathVariable int id) {
        Person newFriend = personRepository.findByPersonId(id);

        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setCode(FriendshipStatusCode.FRIEND);

        Friendship friendship = new Friendship();
        friendship.setStatus(friendshipStatus);
        friendship.setSrcPerson(new Person()); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        friendship.setDstPerson(newFriend);

        FriendResponse addFriend = new FriendResponse();
        addFriend.setError("not error"); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        addFriend.setTimestamp(LocalDateTime.now());
        addFriend.setMessage("ok");

        return new ResponseEntity<>(addFriend, HttpStatus.OK);
    }

}
