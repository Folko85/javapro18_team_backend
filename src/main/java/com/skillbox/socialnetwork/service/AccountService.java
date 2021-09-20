package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final ZoneId UTC = ZoneId.of("UTC");

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountResponse register(RegisterRequest registerRequest) throws RegisterUserExistException {
        if (accountRepository.findByEMail(registerRequest.getEMail()).isPresent())
            throw new RegisterUserExistException();
        AccountResponse registerResponse = new AccountResponse();
        Person person = new Person();
        person.setEMail(registerRequest.getEMail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setConfirmationCode(registerRequest.getCode());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword1()));
        person.setDateAndTimeOfRegistration(LocalDateTime.now(UTC));
        registerResponse.setTimestamp(ZonedDateTime.now(UTC).toEpochSecond());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        registerResponse.setData(dateMap);
        accountRepository.save(person);
        return registerResponse;
    }
    public String sendRecoveryMessage(RecoveryRequest recoveryRequest)
    {


        return "Complete";
    }
}
