package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthResponse;


import com.skillbox.socialnetwork.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity<>(authService.auth(loginRequest), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<AccountResponse> logout() throws Exception {
        return new ResponseEntity<>(authService.logout(), HttpStatus.OK);
    }
}
