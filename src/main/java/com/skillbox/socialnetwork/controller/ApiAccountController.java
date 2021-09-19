package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RequestMapping("/api/v1/account")
public class ApiAccountController {
    private final AccountService accountService;

    public ApiAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody RegisterRequest registerRequest) throws RegisterUserExistException {
      log.info("Сюда что-то дошло с фронта");
        return new ResponseEntity<>(accountService.register(registerRequest), HttpStatus.OK);
    }

    @PutMapping("/recovery")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<AccountResponse> recovery()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
