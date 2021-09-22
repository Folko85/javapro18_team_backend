package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthData;
import com.skillbox.socialnetwork.api.response.AuthDTO.AuthResponse;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.api.security.UserDetailServiceImpl;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.skillbox.socialnetwork.service.AccountService.getAccountResponse;
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
        Person person = accountRepository.findByEMail(loginRequest.getEMail())
                .orElseThrow(()->new UsernameNotFoundException(loginRequest.getEMail()));
        if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            token = jwtProvider.generateToken(loginRequest.getEMail());
        } else throw new UsernameNotFoundException(loginRequest.getEMail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setTimestamp(new Date().getTime() / 1000);
        AuthData authData = new AuthData();
        authData.setToken(token);
//        authData.setEMail(person.getEMail());
//        authData.setAbout(person.getAbout());
//        authData.setBirthDate(person.getBirthday().toEpochDay());
        authResponse.setData(authData);
        return authResponse;
    }

    public AccountResponse logout()
    {
        SecurityContextHolder.clearContext();
        return getAccountResponse(UTC);

    }

}
