package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.exception.*;
import com.skillbox.socialnetwork.service.FriendshipService;
import com.skillbox.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@Tag(name = "Контроллер для работы с профилем")
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;

    }

    @GetMapping("/me")
    @Operation(summary = "Получение текущего пользователя", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<AuthData> getMe(Principal principal) {
        return userService.getUserMe(principal);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получение пользователя по его id", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<AuthData> getUserById(@PathVariable int id, Principal principal) {
        return userService.getUser(id, principal);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Обновить профиль пользователя", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<AuthData> updateUser(@RequestBody AuthData person, Principal principal) throws ApiConnectException {
        return userService.updateUser(person, principal);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Удалить профиль пользователя", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<SuccessResponse> deleteUser(Principal principal) {
        return userService.deleteUser(principal);
    }

    @PutMapping("/block/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Заблокировать пользователя", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<SuccessResponse> blockUser(@PathVariable int id, Principal principal) throws BlockAlreadyExistsException, UserBlocksHimSelfException, BlockingDeletedAccountException {
        return friendshipService.blockUser(principal, id);
    }

    @DeleteMapping("/block/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Разблокировать пользователя", security = @SecurityRequirement(name = "jwt"))
    public DataResponse<SuccessResponse> unBlockUser(@PathVariable int id, Principal principal) throws UnBlockingException, UserUnBlocksHimSelfException, UnBlockingDeletedAccountException {
        return friendshipService.unBlockUser(principal, id);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Поиск пользователя", security = @SecurityRequirement(name = "jwt"))
    public ListResponse<AuthData> search(@RequestParam(name = "first_name", defaultValue = "") String firstName,
                                         @RequestParam(name = "last_name", defaultValue = "") String lastName,
                                         @RequestParam(name = "age_from", defaultValue = "-1") int ageFrom,
                                         @RequestParam(name = "age_to", defaultValue = "-1") int ageTo,
                                         @RequestParam(name = "country", defaultValue = "") String country,
                                         @RequestParam(name = "city", defaultValue = "") String city,
                                         @RequestParam(name = "offset", defaultValue = "0") int offset,
                                         @RequestParam(name = "itemPerPage", defaultValue = "20") int itemPerPage,
                                         Principal principal) {

        return userService.searchPerson(firstName, lastName, ageFrom, ageTo,
                country, city, offset, itemPerPage, principal);
    }
}
