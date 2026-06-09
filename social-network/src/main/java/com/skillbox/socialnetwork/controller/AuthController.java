package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для авторизации.
 */
@Slf4j
@RestController
@Tag(name = "Контроллер для авторизации")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Вход.
     * @param loginRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    @Operation(summary = "login")
    public DataResponse<AuthData> login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authService.auth(loginRequest);
    }

    /**
     * Выход.
     * @return
     */
    @GetMapping("/logout")
    @Operation(summary = "logout")
    public DataResponse<SuccessResponse> logout() {
        return authService.logout();
    }
}
