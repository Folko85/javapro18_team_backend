package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendResponse;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.FriendshipRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
public class FriendshipController {


    private final FriendshipRepository friendshipRepository;
    private final PersonRepository personRepository;
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipRepository friendshipRepository, PersonRepository personRepository, FriendshipService friendshipService) {
        this.friendshipRepository = friendshipRepository;
        this.personRepository = personRepository;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                                   @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                   @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                   Principal principal) {
        return new ResponseEntity<>(friendshipService.getFriends(name, offset, itemPerPage, principal), HttpStatus.OK);
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
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> add(@PathVariable int id, Principal principal) {
        Person newFriend = personRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException(""));

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
