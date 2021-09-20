package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @GetMapping("/api/v1/friends")
    public List<Friendship> list() {
        Iterable<Friendship> taskIterable = friendshipRepository.findAll();
        ArrayList<Friendship> friendsList = new ArrayList<>();
        for (Friendship friendship : taskIterable) {
            friendsList.add(friendship);
        }
        return friendsList;
    }

    @DeleteMapping("/api/v1/friends/{id}")
    public void delete(@PathVariable int id) {
        if (friendshipRepository.existsById(id)) {
            friendshipRepository.deleteById(id);
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
