package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;

import com.skillbox.socialnetwork.exception.BlockAlreadyExistsException;
import com.skillbox.socialnetwork.exception.UnBlockingException;
import com.skillbox.socialnetwork.exception.UserBlocksHimSelfException;
import com.skillbox.socialnetwork.exception.UserUnBlocksHimSelfException;
import com.skillbox.socialnetwork.service.FriendshipService;
import com.skillbox.socialnetwork.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@Tag(name = "Контроллер для получения профиля пользователя, изменения профиля, блокировки/раблокировки")
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;

    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получение текущего пользователя")
    public ResponseEntity<DataResponse> getMe(Principal principal) {

        return new ResponseEntity<>(userService.getUserMe(principal), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Получение пользователя по его id")
    public ResponseEntity<DataResponse> getUserById(@PathVariable int id, Principal principal) {
        return new ResponseEntity<>(userService.getUser(id, principal), HttpStatus.OK);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Обновить профиль пользователя")
    public DataResponse updateUser(@RequestBody AuthData person, Principal principal) {
        return userService.updateUser(person, principal);
    }

    @DeleteMapping("/me")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Удалить профиль пользователя")
    public ResponseEntity<AccountResponse> deleteUser(Principal principal) {
        return new ResponseEntity<>(userService.deleteUser(principal), HttpStatus.OK);
    }

    @PutMapping("/block/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Заблокировать пользователя")
    public ResponseEntity<AccountResponse> blockUser(@PathVariable int id, Principal principal) throws BlockAlreadyExistsException, UserBlocksHimSelfException {
        return new ResponseEntity<>(friendshipService.blockUser(principal, id), HttpStatus.OK);
    }

    @DeleteMapping("/block/{id}")
    @PreAuthorize("hasAuthority('user:write')")
    @Operation(summary = "Разблокировать пользователя")
    public ResponseEntity<AccountResponse> unBlockUser(@PathVariable int id, Principal principal) throws UnBlockingException, UserUnBlocksHimSelfException {
        return new ResponseEntity<>(friendshipService.unBlockUser(principal, id), HttpStatus.OK);
    }
}
