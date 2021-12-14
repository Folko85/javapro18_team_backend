package com.skillbox.socialnetwork.controller;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.socialnetwork.api.request.*;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationSettingData;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
    public DataResponse<SuccessResponse> register(@RequestBody RegisterRequest registerRequest) throws UserExistException, MailjetException {
        return accountService.register(registerRequest);
    }

    @PutMapping("/recovery")
    @Operation(summary = "Восстановление пароля")
    public String recoverySend(@RequestBody RecoveryRequest recoveryRequest) throws MailjetException {
        return accountService.sendRecoveryMessage(recoveryRequest);
    }

    @GetMapping("/recovery_complete")
    @Operation(summary = "Подтверждение восстановления пароля")
    public String recoveryComplete(@RequestParam String key,
                                   @RequestParam String eMail) throws MailjetException {
        return accountService.recoveryComplete(key, eMail);
    }

    @GetMapping("/registration_complete")
    @Operation(summary = "Подтверждение регистрации")
    public String registrationComplete(@RequestParam String key,
                                       @RequestParam String eMail) {
        return accountService.registrationComplete(key, eMail);
    }

    @PutMapping("/email")
    @Operation(summary = "Смена email", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> eMailChange(@RequestBody EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        return accountService.changeEMail(eMailChangeRequest, principal);
    }

    @PutMapping("/password/set")
    @Operation(summary = "Смена пароля", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> passwdChange(@RequestBody PasswdChangeRequest passwdChangeRequest) {
        return accountService.changePasswd(passwdChangeRequest);
    }

    @PutMapping("/notifications")
    @Operation(summary = "Настройка уведомлений", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public DataResponse<SuccessResponse> putNotificationsSetting(@RequestBody NotificationsRequest notificationsRequest, Principal principal) {
        return accountService.setNotificationsSetting(notificationsRequest, principal);
    }

    @GetMapping("/notifications")
    @Operation(summary = "Уведомления", security = @SecurityRequirement(name = "jwt"))
    @PreAuthorize("hasAuthority('user:write')")
    public ListResponse<NotificationSettingData> getNotificationsSetting(Principal principal) {
        return accountService.getNotificationsSetting(principal);
    }
}
