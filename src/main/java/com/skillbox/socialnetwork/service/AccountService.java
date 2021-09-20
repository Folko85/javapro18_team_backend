package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.RegisterUserExistException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {
    private final ZoneId UTC = ZoneId.of("UTC");
    private final String RECOVERY_URL = "http://localhost:8080/api/v1/account/recovery_complete?";
    private final String REGISTRATION_URL = "http://localhost:8080/api/v1/account/registration_complete?";

    private final AccountRepository accountRepository;
    private final MailSender mailSender;


    public AccountService(AccountRepository accountRepository, MailSender mailSender) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
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
        String code = UUID.randomUUID().toString().replace("-", "");
        mailSender.send(registerRequest.getEMail(), REGISTRATION_URL + "key=" + code + "&eMail=" + registerRequest.getEMail());
        person.setConfirmationCode(code);
        registerResponse.setTimestamp(ZonedDateTime.now(UTC).toEpochSecond());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        registerResponse.setData(dateMap);
        accountRepository.save(person);
        return registerResponse;
    }

    public String sendRecoveryMessage(RecoveryRequest recoveryRequest) {
        Person person = findPerson(recoveryRequest.getEMail());
        String code = UUID.randomUUID().toString().replace("-", "");
        person.setConfirmationCode(code);
        accountRepository.save(person);
        mailSender.send(recoveryRequest.getEMail(), RECOVERY_URL + "key=" + code + "&eMail=" + recoveryRequest.getEMail());
        return "Сообщение отправлено на почту";
    }

    public String recoveryComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            String passwd = UUID.randomUUID().toString().replace("-", "");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            person.setPassword(passwordEncoder.encode(passwd));
            person.setConfirmationCode("");
            mailSender.send(eMail, passwd);
            accountRepository.save(person);
        } else return "Неверный код";
        return "Новый пароль выслан";
    }

    public String registrationComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            person.setApproved(true);
            person.setConfirmationCode("");
            accountRepository.save(person);
        } else return "Неверный код";
        return "Аккаунт подтверждён";
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
