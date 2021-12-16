package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.ResponseFriendsList;
import com.skillbox.socialnetwork.exception.*;
import com.skillbox.socialnetwork.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ListResponse<AuthData> findFriend(@RequestParam(name = "name", defaultValue = "") String name,
                                             @RequestParam(name = "offset", defaultValue = "0") int offset,
                                             @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                             Principal principal) {
        return friendshipService.getFriends(name, offset, itemPerPage, principal);
    }

    @Operation(summary = "Удаление пользователя",
            description = "Удаление пользователя из друзей", security = @SecurityRequirement(name = "jwt"))
    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> stopBeingFriends(@PathVariable int id, Principal principal) throws FriendshipNotFoundException {
        return friendshipService.stopBeingFriendsById(id, principal);

    }

    @Operation(summary = "Добавление в друзья",
            description = "Принть/добавить пользователя в друзья", security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> addingToFriends(@PathVariable int id, Principal principal) throws AddingOrSubscribingOnBlockerPersonException, DeletedAccountException, AddingOrSubscribingOnBlockedPersonException, AddingYourselfToFriends, FriendshipExistException {
        return friendshipService.addNewFriend(id, principal);

    }

    @Operation(summary = "Список заявок",
            description = "Получить список заявок", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends/request")
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<AuthData> getFriendsRequests(@RequestParam(name = "name", defaultValue = "") String name,
                                                     @RequestParam(name = "offset", defaultValue = "0") int offset,
                                                     @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                     Principal principal) {
        return friendshipService.getFriendsRequests(name, offset, itemPerPage, principal);
    }

    @Operation(summary = "Рекомендации",
            description = "Получить список рекомендаций", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends/recommendations")
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<AuthData> getRecommendedUsers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                      @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                      Principal principal) {
        return friendshipService.recommendedUsers(offset, itemPerPage, principal);
    }

    @Operation(summary = "Являются ли пользователи друзьями",
            description = "Получить информацию является ли пользователь другом указанных пользователей", security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/is/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseFriendsList isFriends(@RequestBody IsFriends isFriends, Principal principal) {
        return friendshipService.isPersonsFriends(isFriends, principal);
    }

}
