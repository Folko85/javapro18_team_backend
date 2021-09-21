package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthResponse;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.api.security.UserDetailServiceImpl;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.time.ZoneOffset.UTC;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailService;

    public AuthService(AccountRepository accountRepository,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider,
                       UserDetailServiceImpl userDetailService) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.userDetailService = userDetailService;
    }


    public AuthResponse auth(LoginRequest loginRequest) {
        UserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getEMail());
        String token;
        if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            token = jwtProvider.generateToken(loginRequest.getEMail());
        } else throw new UsernameNotFoundException(loginRequest.getEMail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTimestamp(new Date().getTime() / 1000);
        Map<String, String> data = new HashMap<>();
        AuthData authData = new AuthData();
        authData.setToken(token);
        authResponse.setData(authData);
        return authResponse;
    }

    public AccountResponse logout()
    {
        SecurityContextHolder.clearContext();
        AccountResponse logoutResponse = new AccountResponse();
        logoutResponse.setTimestamp(ZonedDateTime.now(UTC).toEpochSecond());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        logoutResponse.setData(dateMap);
        return logoutResponse;
    }

}
