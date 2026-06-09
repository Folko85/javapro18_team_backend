package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.IsFriends;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.response.friendsdto.ResponseFriendsList;
import com.skillbox.socialnetwork.exception.AddingOrSubscribingOnBlockedPersonException;
import com.skillbox.socialnetwork.exception.AddingOrSubscribingOnBlockerPersonException;
import com.skillbox.socialnetwork.exception.AddingYourselfToFriends;
import com.skillbox.socialnetwork.exception.DeletedAccountException;
import com.skillbox.socialnetwork.exception.FriendshipExistException;
import com.skillbox.socialnetwork.exception.FriendshipNotFoundException;
import com.skillbox.socialnetwork.service.FriendshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Работа с друзьями.
 */
@RestController
@Tag(name = "Друзья", description = "Работа с друзьями")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    /**
     * Получить список друзей.
     *
     * @param name
     * @param offset
     * @param itemPerPage
     * @param principal
     * @return
     */
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

    /**
     * Удаление пользователя из друзей.
     *
     * @param id
     * @param principal
     * @return
     * @throws FriendshipNotFoundException
     */
    @Operation(summary = "Удаление пользователя",
            description = "Удаление пользователя из друзей", security = @SecurityRequirement(name = "jwt"))
    @DeleteMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> stopBeingFriends(@PathVariable int id, Principal principal) throws FriendshipNotFoundException {
        return friendshipService.stopBeingFriendsById(id, principal);

    }

    /**
     * Принять/добавить пользователя в друзья.
     *
     * @param id
     * @param principal
     * @return
     * @throws AddingOrSubscribingOnBlockerPersonException
     * @throws DeletedAccountException
     * @throws AddingOrSubscribingOnBlockedPersonException
     * @throws AddingYourselfToFriends
     * @throws FriendshipExistException
     */
    @Operation(summary = "Добавление в друзья",
            description = "Принять/добавить пользователя в друзья", security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/friends/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> addingToFriends(@PathVariable int id, Principal principal)
            throws AddingOrSubscribingOnBlockerPersonException, DeletedAccountException,
            AddingOrSubscribingOnBlockedPersonException, AddingYourselfToFriends, FriendshipExistException {
        return friendshipService.addNewFriend(id, principal);

    }

    /**
     * Получить список заявок.
     *
     * @param name
     * @param offset
     * @param itemPerPage
     * @param principal
     * @return
     */
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

    /**
     * Получить список рекомендаций.
     *
     * @param offset
     * @param itemPerPage
     * @param principal
     * @return
     */
    @Operation(summary = "Рекомендации",
            description = "Получить список рекомендаций", security = @SecurityRequirement(name = "jwt"))
    @GetMapping("/api/v1/friends/recommendations")
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<AuthData> getRecommendedUsers(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                                      @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                                      Principal principal) {
        return friendshipService.recommendedUsers(offset, itemPerPage, principal);
    }

    /**
     * Являются ли пользователи друзьями.
     *
     * @param isFriends
     * @param principal
     * @return
     */
    @Operation(summary = "Являются ли пользователи друзьями",
            description = "Получить информацию является ли пользователь другом указанных пользователей",
            security = @SecurityRequirement(name = "jwt"))
    @PostMapping("/api/v1/is/friends")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseFriendsList isFriends(@RequestBody IsFriends isFriends, Principal principal) {
        return friendshipService.isPersonsFriends(isFriends, principal);
    }

}
