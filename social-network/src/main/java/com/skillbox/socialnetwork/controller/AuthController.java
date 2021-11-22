package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;


import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "Контроллер для авторизации")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "login")
    public ResponseEntity<DataResponse<AuthData>> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity<>(authService.auth(loginRequest), HttpStatus.OK);
    }

    @GetMapping("/logout")
    @Operation(summary = "logout")
    public ResponseEntity<AccountResponse> logout() {
        return new ResponseEntity<>(authService.logout(), HttpStatus.OK);
    }
}
