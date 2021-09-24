package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.*;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RestController
@RequestMapping("/api/v1/account")
public class ApiAccountController {
    private final AccountService accountService;

    public ApiAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> register(@RequestBody RegisterRequest registerRequest) throws UserExistException {
        log.info("Сюда что-то дошло с фронта");
        return new ResponseEntity<>(accountService.register(registerRequest), HttpStatus.OK);
    }

    @PutMapping("/recovery")
    public ResponseEntity<String> recoverySend(@RequestBody RecoveryRequest recoveryRequest) {
        return new ResponseEntity<>(accountService.sendRecoveryMessage(recoveryRequest), HttpStatus.OK);
    }

    @GetMapping("/recovery_complete")
    public ResponseEntity<String> recoveryComplete(@RequestParam String key,
                                                   @RequestParam String eMail) {
        return new ResponseEntity<>(accountService.recoveryComplete(key, eMail), HttpStatus.OK);
    }

    @GetMapping("/registration_complete")
    public ResponseEntity<String> registrationComplete(@RequestParam String key,
                                                       @RequestParam String eMail) {
        return new ResponseEntity<>(accountService.registrationComplete(key, eMail), HttpStatus.OK);
    }

    @PutMapping("/email")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> eMailChange(@RequestBody EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        return new ResponseEntity<>(accountService.changeEMail(eMailChangeRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/password/set")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> passwdChange(@RequestBody PasswdChangeRequest passwdChangeRequest) {
        return new ResponseEntity<>(accountService.changePasswd(passwdChangeRequest), HttpStatus.OK);
    }
    @PutMapping("/notifications")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> notifications(@RequestBody NotificationsRequest notificationsRequest, Principal principal)
    {
        return new ResponseEntity<>(accountService.setNotifications(notificationsRequest,principal),HttpStatus.OK);
    }
}
