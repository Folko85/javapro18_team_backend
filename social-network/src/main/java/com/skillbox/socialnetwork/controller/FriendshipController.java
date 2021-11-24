package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.FriendsResponse200;
import com.skillbox.socialnetwork.api.response.friendsdto.friendsornotfriends.ResponseFriendsList;
import com.skillbox.socialnetwork.exception.*;
import com.skillbox.socialnetwork.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "Друзья", description = "Работа с друзьями")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @Operation(summary = "Список друзей",
            description = "Получить список друзей", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<AuthData>> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                                             @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                             @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                             Principal principal) {
        return new ResponseEntity<>(friendshipService.getFriends(name, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя",
            description = "Удаление пользователя из друзей", security = @SecurityRequirement(name = "jwt"))
    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<FriendsResponse200> stopBeingFriends(@PathVariable int id, Principal principal) throws FriendshipNotFoundException {
        return new ResponseEntity<>(friendshipService.stopBeingFriendsById(id, principal), HttpStatus.OK);

    }

    @Operation(summary = "Добавление в друзья",
            description = "Принть/добавить пользователя в друзья", security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<FriendsResponse200> addingToFriends(@PathVariable int id, Principal principal) throws AddingOrSubscribingOnBlockerPersonException, DeletedAccountException, AddingOrSubscribingOnBlockedPersonException, AddingYourselfToFriends, FriendshipExistException {
        return new ResponseEntity<>(friendshipService.addNewFriend(id, principal), HttpStatus.OK);

    }

    @Operation(summary = "Список заявок",
            description = "Получить список заявок", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends/request")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<AuthData>> getFriendsRequests(@RequestParam(name = "name", defaultValue = "") String name,
                                                                     @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                                     @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                                     Principal principal) {
        return new ResponseEntity<>(friendshipService.getFriendsRequests(name, offset, itemPerPage, principal), HttpStatus.OK);
    }

    @Operation(summary = "Рекомендации",
            description = "Получить список рекомендаций", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends/recommendations")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<AuthData>> getRecommendedUsers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                                      @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                                      Principal principal) {
        return new ResponseEntity<>(friendshipService.recommendedUsers(offset, itemPerPage, principal), HttpStatus.OK);
    }

    @Operation(summary = "Являются ли пользователи друзьями",
            description = "Получить информацию является ли пользователь другом указанных пользователей", security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/is/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ResponseFriendsList> isFriends(@RequestBody IsFriends isFriends, Principal principal) {
        return new ResponseEntity<>(friendshipService.isPersonsFriends(isFriends, principal), HttpStatus.OK);
    }

}
