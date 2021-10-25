package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsOrNotFriends.ResponseFriendsList;
import com.skillbox.socialnetwork.service.FriendshipService;
import com.skillbox.socialnetwork.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "Друзья", description = "Работа с друзьями")
public class FriendshipController {

    private final PersonService personService;
    private final FriendshipService friendshipService;

    public FriendshipController(PersonService personService, FriendshipService friendshipService) {
        this.personService = personService;
        this.friendshipService = friendshipService;
    }

    @Operation(summary = "Список друзей",
            description = "Получить список друзей")
    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                        @RequestParam(name = "offset", defaultValue = "0") int offset,
                                        @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                        Principal principal) {

        ListResponse listResponse = friendshipService.getFriends(name, offset, itemPerPage, principal);
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя",
            description = "Удаление пользователя из друзей")
    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> stopBeingFriends(@PathVariable int id, Principal principal) {

        FriendsResponse200 response200 = friendshipService.stopBeingFriendsById(id, principal);
        return new ResponseEntity<>(response200, HttpStatus.OK);

    }

    @Operation(summary = "Добавление в друзья",
            description = "Принть/добавить пользователя в друзья")
    @PostMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> addingToFriends(@PathVariable int id, Principal principal) {

        FriendsResponse200 response200 = friendshipService.addNewFriend(id, principal);

        if (response200 != null) {
            return new ResponseEntity<>(response200, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Operation(summary = "Список заявок",
            description = "Получить список заявок")
    @GetMapping("/api/v1/friends/request")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> listApplications(@RequestParam(name = "name", defaultValue = "") String name,
                                              @RequestParam(name = "offset", defaultValue = "0") int offset,
                                              @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                              Principal principal) {

        ListResponse listOfApplications = friendshipService.getListOfApplications(name, offset, itemPerPage, principal);
        return new ResponseEntity<>(listOfApplications, HttpStatus.OK);
    }

    @Operation(summary = "Рекомендации",
            description = "Получить список рекомендаций")
    @GetMapping("/api/v1/friends/recommendations")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> getRecommendedUsers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                 @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                 Principal principal) {

        ListResponse listResponse = friendshipService.recommendedUsers(offset, itemPerPage, principal);
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

    @Operation(summary = "Являются ли пользователи друзьями",
            description = "Получить информацию является ли пользователь другом указанных пользователей")
    @PostMapping("/api/v1/is/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> isFriends(@RequestBody IsFriends isFriends, Principal principal) {

        ResponseFriendsList personsFriends = friendshipService.isPersonsFriends(isFriends, principal);
        return new ResponseEntity<>(personsFriends, HttpStatus.OK);
    }

}
