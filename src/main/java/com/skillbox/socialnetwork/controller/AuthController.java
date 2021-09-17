package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.LoginResponse;


import com.skillbox.socialnetwork.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return new ResponseEntity<>(authService.getToken(loginRequest), HttpStatus.OK);
    }

}
