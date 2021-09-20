package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/account/")
public class ApiAccountController {
    private final AccountService accountService;

    public ApiAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody RegisterRequest registerRequest) throws RegisterUserExistException {

        return new ResponseEntity<>(accountService.register(registerRequest), HttpStatus.OK);
    }
    @PutMapping("/recovery")
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> recovery(@RequestBody RecoveryRequest recoveryRequest)
    {
        return new ResponseEntity<>(accountService.sendRecoveryMessage(recoveryRequest),HttpStatus.OK);
    }

}
