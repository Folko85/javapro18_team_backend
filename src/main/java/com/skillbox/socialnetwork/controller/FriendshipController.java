package com.skillbox.socialnetwork.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnetwork.api.response.AuthDTO.Place;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping("/api/v1/friends")
    public ResponseEntity<?> list() {

        Iterable<Friendship> taskIterable = friendshipRepository.findAll();

        ArrayList<Friendship> friendsList = new ArrayList<>();

        for (Friendship friendship : taskIterable) {
            Person person = friendship.getDstPerson();

            friendsList.add(friendship);
        }

        if (!friendsList.isEmpty()) {
            return new ResponseEntity<>(friendsList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/api/v1/friends/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        if (friendshipRepository.existsById(id)) {
            friendshipRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/api/v1/friends/{id}")
    public ResponseEntity<?> add(@PathVariable int id, Friendship friendship) {
        Optional<Friendship> optionalFriendship = friendshipRepository.findById(id);

        if (optionalFriendship.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        friendshipRepository.save(friendship);
        return new ResponseEntity<>(friendshipRepository.findById(id), HttpStatus.OK);
    }

}
