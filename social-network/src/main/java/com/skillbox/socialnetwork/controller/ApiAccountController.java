package com.skillbox.socialnetwork.controller;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.socialnetwork.api.request.*;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationSettingData;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Slf4j
@RestController
@Tag(name = "Контроллер для работы с созданием учётной записи и безопасностью")
@RequestMapping("/api/v1/account")
public class ApiAccountController {
    private final AccountService accountService;

    public ApiAccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация")
    public ResponseEntity<AccountResponse> register(@RequestBody RegisterRequest registerRequest) throws UserExistException, MailjetException {
        return new ResponseEntity<>(accountService.register(registerRequest), HttpStatus.OK);
    }

    @PutMapping("/recovery")
    @Operation(summary = "Восстановление пароля")
    public ResponseEntity<String> recoverySend(@RequestBody RecoveryRequest recoveryRequest) throws MailjetException {
        return new ResponseEntity<>(accountService.sendRecoveryMessage(recoveryRequest), HttpStatus.OK);
    }

    @GetMapping("/recovery_complete")
    @Operation(summary = "Подтверждение восстановления пароля")
    public ResponseEntity<String> recoveryComplete(@RequestParam String key,
                                                   @RequestParam String eMail) throws MailjetException {
        return new ResponseEntity<>(accountService.recoveryComplete(key, eMail), HttpStatus.OK);
    }

    @GetMapping("/registration_complete")
    @Operation(summary = "Подтверждение регистрации")
    public ResponseEntity<String> registrationComplete(@RequestParam String key,
                                                       @RequestParam String eMail) {
        return new ResponseEntity<>(accountService.registrationComplete(key, eMail), HttpStatus.OK);
    }

    @PutMapping("/email")
    @Operation(summary = "Смена email", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> eMailChange(@RequestBody EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        return new ResponseEntity<>(accountService.changeEMail(eMailChangeRequest, principal), HttpStatus.OK);
    }

    @PutMapping("/password/set")
    @Operation(summary = "Смена пароля", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> passwdChange(@RequestBody PasswdChangeRequest passwdChangeRequest) {
        return new ResponseEntity<>(accountService.changePasswd(passwdChangeRequest), HttpStatus.OK);
    }

    @PutMapping("/notifications")
    @Operation(summary = "Уведомления", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<AccountResponse> putNotificationsSetting(@RequestBody NotificationsRequest notificationsRequest, Principal principal) {
        return new ResponseEntity<>(accountService.setNotificationsSetting(notificationsRequest, principal), HttpStatus.OK);
    }
    @GetMapping("/notifications")
    @Operation(summary = "Уведомления", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<ListResponse<NotificationSettingData>> getNotificationsSetting(Principal principal) {
        return new ResponseEntity<>(accountService.getNotificationsSetting(principal), HttpStatus.OK);
    }
}
