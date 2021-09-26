package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.GetFriendsListRequest;
import com.skillbox.socialnetwork.api.response.FriendsDTO.FriendResponse;
import com.skillbox.socialnetwork.api.response.FriendsDTO.FriendsPojo;
import com.skillbox.socialnetwork.api.response.FriendsDTO.FriendsList;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class FriendshipController {

    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;

    @Autowired
    public FriendshipController(FriendshipRepository friendshipRepository, PersonRepository personRepository) {
        this.friendshipRepository = friendshipRepository;
        this.personRepository = personRepository;
    }

    @GetMapping("/api/v1/friends")
    public ResponseEntity<?> findFriend(@RequestBody GetFriendsListRequest getFriendsListRequest) {

        Person person = personRepository.findPersonByName(getFriendsListRequest.getName());
        Set<Person> personList = friendshipRepository.findByName(person);

        FriendsList response = new FriendsList();
        response.setTotal(personList.size());
        response.setTimestamp(LocalDateTime.now());

        if (personList.size() > 0) {
            Set<FriendsPojo> friendsList = personList
                    .stream()
                    .map(Converter::friendsToPojo)
                    .collect(Collectors.toSet());

            return new ResponseEntity<>(friendsList, HttpStatus.OK);
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
        // ищем пользователя в БД
        Person newFriend = personRepository.findPersonById(id);

        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setTime(LocalDateTime.now());
        friendshipStatus.setCode(FriendshipStatusCode.FRIEND);

        Friendship friendship = new Friendship();
        friendship.setStatus(friendshipStatus);
        friendship.setSrcPerson(new Person()); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        friendship.setDstPerson(newFriend);

        friendshipRepository.save(friendship);

        FriendResponse addFriend = new FriendResponse();
        addFriend.setError("not error"); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        addFriend.setTimestamp(LocalDateTime.now());
        addFriend.setMessage("ok");

        return new ResponseEntity<>(addFriend, HttpStatus.OK);
    }

}
