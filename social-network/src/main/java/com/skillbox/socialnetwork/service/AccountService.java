package com.skillbox.socialnetwork.service;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.socialnetwork.api.request.EMailChangeRequest;
import com.skillbox.socialnetwork.api.request.NotificationsRequest;
import com.skillbox.socialnetwork.api.request.PasswdChangeRequest;
import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AccountService {

    private final PersonRepository personRepository;
    private final MailSender mailSender;
    private final JwtProvider jwtProvider;

    @Value("${registration.confirm.url}")
    private String regUrl;

    @Value("${registration.confirm.need}")
    private boolean needConfirm;

    public AccountService(PersonRepository personRepository,
                          MailSender mailSender,
                          JwtProvider jwtProvider) {
        this.personRepository = personRepository;
        this.mailSender = mailSender;
        this.jwtProvider = jwtProvider;
    }

    public AccountResponse register(RegisterRequest registerRequest) throws UserExistException, MailjetException {
        if (personRepository.findByEMail(registerRequest.getEMail()).isPresent())
            throw new UserExistException();
        Person person = new Person();
        person.setEMail(registerRequest.getEMail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setConfirmationCode(registerRequest.getCode());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword1()));
        person.setDateAndTimeOfRegistration(LocalDateTime.now(ZoneOffset.UTC));
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setLastOnlineTime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        String code = UUID.randomUUID().toString().replace("-", "");
        if (needConfirm) {
            mailSender.send(registerRequest.getEMail(), regUrl + "?key=" + code + "&eMail=" + registerRequest.getEMail());
            person.setConfirmationCode(code);
        } else {
            person.setApproved(true);
        }
        person.setRole(Role.USER);
        personRepository.save(person);
        return getAccountResponse();
    }

    public String sendRecoveryMessage(RecoveryRequest recoveryRequest) throws MailjetException {
        Person person = findPerson(recoveryRequest.getEMail());
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        person.setConfirmationCode(code);
        personRepository.save(person);

        mailSender.send(recoveryRequest.getEMail(), "Enter this code:" + code);
        return code;
    }

    public String recoveryComplete(String key, String eMail) throws MailjetException {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            String passwd = UUID.randomUUID().toString().replace("-", "");
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            person.setPassword(passwordEncoder.encode(passwd));
            person.setConfirmationCode("");
            mailSender.send(eMail, passwd);
            personRepository.save(person);
        } else throw new EntityNotFoundException("");
        return "Новый пароль выслан";
    }

    public String registrationComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            person.setApproved(true);
            person.setConfirmationCode("");
            personRepository.save(person);
        } else throw new EntityNotFoundException("");
        return "Аккаунт подтверждён";
    }

    public AccountResponse changeEMail(EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        if (personRepository.findByEMail(eMailChangeRequest.getEMail()).isPresent())
            throw new UserExistException();
        Person person = findPerson(principal.getName());
        person.setEMail(eMailChangeRequest.getEMail());
        SecurityContextHolder.clearContext();
        personRepository.save(person);
        return getAccountResponse();

    }

    public AccountResponse setNotifications(NotificationsRequest notificationsRequest, Principal principal) {
        Person person = findPerson(principal.getName());
        return getAccountResponse();
    }

    public AccountResponse changePasswd(PasswdChangeRequest passwdChangeRequest) {
        Person person = findPerson(jwtProvider.getLoginFromToken(passwdChangeRequest.getToken()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(passwdChangeRequest.getPassword()));
        personRepository.save(person);
        return getAccountResponse();
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

    static AccountResponse getAccountResponse() {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setTimestamp(ZonedDateTime.now().toInstant());
        Map<String, String> dateMap = new HashMap<>();
        dateMap.put("message", "ok");
        accountResponse.setData(dateMap);
        return accountResponse;
    }

}
