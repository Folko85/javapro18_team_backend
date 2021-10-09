package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.*;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
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
    private final JwtProvider jwtProvider;


    public AccountService(AccountRepository accountRepository,
                          MailSender mailSender,
                          JwtProvider jwtProvider) {
        this.accountRepository = accountRepository;
        this.mailSender = mailSender;
        this.jwtProvider = jwtProvider;
    }

    public AccountResponse register(RegisterRequest registerRequest) throws UserExistException {
        if (accountRepository.findByEMail(registerRequest.getEMail()).isPresent())
            throw new UserExistException();
        Person person = new Person();
        person.setEMail(registerRequest.getEMail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setConfirmationCode(registerRequest.getCode());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword1()));
        person.setDateAndTimeOfRegistration(LocalDateTime.now(UTC));
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setLastOnlineTime(ZonedDateTime.now(UTC).toLocalDateTime());
        String code = UUID.randomUUID().toString().replace("-", "");
        //mailSender.send(registerRequest.getEMail(), REGISTRATION_URL + "key=" + code + "&eMail=" + registerRequest.getEMail());
        person.setConfirmationCode(code);
        accountRepository.save(person);
        return getAccountResponse(UTC);
    }

    public String sendRecoveryMessage(RecoveryRequest recoveryRequest) {
        Person person = findPerson(recoveryRequest.getEMail());
        String code = UUID.randomUUID().toString().replace("-", "").substring(0,4);
        person.setConfirmationCode(code);
        accountRepository.save(person);

       // mailSender.send(recoveryRequest.getEMail(), RECOVERY_URL + "key=" + code + "&eMail=" + recoveryRequest.getEMail());
        return code;
    }

    public String recoveryComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            String passwd = UUID.randomUUID().toString().replace("-", "");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            person.setPassword(passwordEncoder.encode(passwd));
            person.setConfirmationCode("");
            //  mailSender.send(eMail, passwd);
            accountRepository.save(person);
        } else throw new EntityNotFoundException("");
        return "Новый пароль выслан";
    }

    public String registrationComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            person.setApproved(true);
            person.setConfirmationCode("");
            accountRepository.save(person);
        } else throw new EntityNotFoundException("");
        return "Аккаунт подтверждён";
    }

    public AccountResponse changeEMail(EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        if (accountRepository.findByEMail(eMailChangeRequest.getEMail()).isPresent())
            throw new UserExistException();
        Person person = findPerson(principal.getName());
        person.setEMail(eMailChangeRequest.getEMail());
        SecurityContextHolder.clearContext();
        accountRepository.save(person);
        return getAccountResponse(UTC);

    }
    public AccountResponse setNotifications(NotificationsRequest notificationsRequest, Principal principal)
    {
        Person person = findPerson(principal.getName());
        return getAccountResponse(UTC);
    }
    public AccountResponse changePasswd(PasswdChangeRequest passwdChangeRequest) {
        Person person = findPerson(jwtProvider.getLoginFromToken(passwdChangeRequest.getToken()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(passwdChangeRequest.getPassword()));
        accountRepository.save(person);
        return getAccountResponse(UTC);
    }

    private Person findPerson(String eMail) {
        return accountRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    static AccountResponse getAccountResponse(ZoneId utc) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setTimestamp(ZonedDateTime.now().toInstant());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        accountResponse.setData(dateMap);
        return accountResponse;
    }
}
