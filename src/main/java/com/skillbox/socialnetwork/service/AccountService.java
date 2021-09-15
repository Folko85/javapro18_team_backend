package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.RegisterResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.repository.AccountRepository;
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

    public RegisterResponse register(RegisterRequest registerRequest) throws RegisterUserExistException {
        if (accountRepository.findByEMail(registerRequest.getEMail()).isPresent())
            throw new RegisterUserExistException();
        RegisterResponse registerResponse = new RegisterResponse();
        Person person = new Person();
        person.setEMail(registerRequest.getEMail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setConfirmationCode(registerRequest.getCode());
        person.setPassword(registerRequest.getPassword1());
        person.setDateAndTimeOfRegistration(LocalDateTime.now(UTC));
        registerResponse.setTimestamp(ZonedDateTime.now(UTC).toEpochSecond());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        registerResponse.setData(dateMap);
        accountRepository.save(person);
        return registerResponse;
    }
}
