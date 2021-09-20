package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
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
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<String> recoverySend(@RequestBody RecoveryRequest recoveryRequest)
    {
        return new ResponseEntity<>(accountService.sendRecoveryMessage(recoveryRequest),HttpStatus.OK);
    }
    @GetMapping("/recovery_complete")
    public ResponseEntity<String> recoveryComplete(@RequestParam String key,
                                                   @RequestParam String eMail)
    {
        return new ResponseEntity<>(accountService.recoveryComplete(key,eMail),HttpStatus.OK);
    }
    @GetMapping("/registration_complete")
    public ResponseEntity<String> registrationComplete(@RequestParam String key,
                                                   @RequestParam String eMail)
    {
        return new ResponseEntity<>(accountService.registrationComplete(key,eMail),HttpStatus.OK);
    }
}
