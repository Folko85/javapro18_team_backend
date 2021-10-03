package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.GetFriendsListRequest;
import com.skillbox.socialnetwork.api.response.friendsDTO.FriendsDto;
import com.skillbox.socialnetwork.api.response.friendsDTO.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.postDTO.PostResponse;
import com.skillbox.socialnetwork.entity.Friendship;
import com.skillbox.socialnetwork.entity.FriendshipStatus;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.service.FriendshipService;
import com.skillbox.socialnetwork.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FriendshipController {

    private final PersonService personService;
    private final FriendshipService friendshipService;

    public FriendshipController(PersonService personService, FriendshipService friendshipService) {
        this.personService = personService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> findMyFriend(@RequestBody GetFriendsListRequest getFriendsListRequest) {

        String friendsName = getFriendsListRequest.getName();
        int itemPerPage = getFriendsListRequest.getItemPerPage();

        List<Person> myFriends = friendshipService.findMyFriendByName(friendsName, itemPerPage);

        PostResponse response = new PostResponse();
        response.setTotal(myFriends.size());
        response.setTimestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC));

        if (myFriends.size() > 0) {
            List<FriendsDto> friendsList = myFriends
                    .stream()
                    .map(personService::friendsToPojo)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(friendsList, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> stopBeingFriends(@PathVariable int id) {

        Optional<Friendship> friendship = friendshipService.findMyFriendshipByIdMyFriend(id);

        if (friendship.isPresent()) {
            friendshipService.stopBeingFriendsById(id);

            FriendsResponse200 response = new FriendsResponse200();
            response.setError("Successfully");
            response.setTimestamp(LocalDateTime.now());
            response.setMessage("Stop being friends");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/api/v1/friends{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> addingToFriends(@PathVariable int id, Principal principal) {

        String eMailSrcPerson = principal.getName();

        Person srcPerson = personService.findPersonByEmail(eMailSrcPerson);
        Optional<Person> optionalPerson = personService.findPersonById(id);

        if (optionalPerson.isPresent() && friendshipService.findMyFriendshipByIdMyFriend(id).isEmpty()) {

            Person dstPerson = optionalPerson.get();

            FriendshipStatus friendshipStatus = new FriendshipStatus();
            friendshipStatus.setTime(LocalDateTime.now());
            friendshipStatus.setCode(FriendshipStatusCode.FRIEND);

            Friendship newFriendship = new Friendship();
            newFriendship.setStatus(friendshipStatus);
            newFriendship.setSrcPerson(srcPerson);
            newFriendship.setDstPerson(dstPerson);

            friendshipService.save(newFriendship);

            FriendsResponse200 addFriendResponse = new FriendsResponse200();
            addFriendResponse.setError("Successfully");
            addFriendResponse.setTimestamp(LocalDateTime.now());
            addFriendResponse.setMessage("Adding to friends");

            return new ResponseEntity<>(addFriendResponse, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
