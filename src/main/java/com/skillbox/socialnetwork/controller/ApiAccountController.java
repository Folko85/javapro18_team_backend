package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.RegisterResponse;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/account/")
public class ApiAccountController {
    private final AccountService accountService;

    public ApiAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) throws RegisterUserExistException {

        return new ResponseEntity<>(accountService.register(registerRequest), HttpStatus.OK);
    }
}
