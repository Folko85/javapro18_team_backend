package com.skillbox.socialnetwork.service;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.socialnetwork.api.request.*;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.ListResponse;
import com.skillbox.socialnetwork.api.response.SuccessResponse;
import com.skillbox.socialnetwork.api.response.notificationdto.NotificationSettingData;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.config.property.RegistrationProperties;
import com.skillbox.socialnetwork.entity.NotificationSetting;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.MessagesPermission;
import com.skillbox.socialnetwork.entity.enums.NotificationType;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.exception.UserExistException;
import com.skillbox.socialnetwork.repository.NotificationSettingRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final PersonRepository personRepository;
    private final MailSender mailSender;
    private final JwtProvider jwtProvider;
    private final RegistrationProperties registrationProperties;
    private final NotificationSettingRepository notificationSettingRepository;

    public DataResponse<SuccessResponse> register(RegisterRequest registerRequest) throws UserExistException, MailjetException {
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
        if (registrationProperties.isNeed()) {
            mailSender.send(registerRequest.getEMail(), registrationProperties.getUrl() + "?key=" + code + "&eMail=" + registerRequest.getEMail());
            person.setConfirmationCode(code);
        } else {
            person.setApproved(true);
        }
        person.setRole(Role.USER);
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));

    }

    public String sendRecoveryMessage(RecoveryRequest recoveryRequest) throws MailjetException {
        Person person = findPerson(recoveryRequest.getEMail());
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        person.setConfirmationCode(code);
        personRepository.save(person);

        mailSender.send(recoveryRequest.getEMail(), "Enter this code:" + code);
        return "Код подтверждения выслан";
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

    public DataResponse<SuccessResponse> changeEMail(EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        if (personRepository.findByEMail(eMailChangeRequest.getEMail()).isPresent()) {
            throw new UserExistException();
        }
        Person person = findPerson(principal.getName());
        person.setEMail(eMailChangeRequest.getEMail());
        SecurityContextHolder.clearContext();
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));

    }

    public DataResponse<SuccessResponse> setNotificationsSetting(NotificationsRequest notificationsRequest, Principal principal) {
        Person person = findPerson(principal.getName());
        NotificationSetting notificationSetting = notificationSettingRepository.findNotificationSettingByPersonId(person.getId())
                .orElse(new NotificationSetting().setFriendsRequest(true).setCommentComment(true).setPostComment(true).setPerson(person));
        switch (notificationsRequest.getNotificationType()) {
            case FRIEND_REQUEST -> notificationSetting.setFriendsRequest(notificationsRequest.isEnable());
            case POST_COMMENT -> notificationSetting.setPostComment(notificationsRequest.isEnable());
            case COMMENT_COMMENT -> notificationSetting.setCommentComment(notificationsRequest.isEnable());
        }
        notificationSettingRepository.save(notificationSetting);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));
    }

    public ListResponse<NotificationSettingData> getNotificationsSetting(Principal principal) {
        Person person = findPerson(principal.getName());
        NotificationSetting notificationSetting = notificationSettingRepository.findNotificationSettingByPersonId(person.getId())
                .orElse(new NotificationSetting().setFriendsRequest(true).setCommentComment(true).setPostComment(true));
        ListResponse<NotificationSettingData> dataResponse = new ListResponse<>();
        dataResponse.setTimestamp(Instant.now());
        List<NotificationSettingData> list = new ArrayList<>();
        list.add(new NotificationSettingData().setNotificationType(NotificationType.FRIEND_REQUEST)
                .setEnable(notificationSetting.isFriendsRequest()));
        list.add(new NotificationSettingData().setNotificationType(NotificationType.POST_COMMENT)
                .setEnable(notificationSetting.isPostComment()));
        list.add(new NotificationSettingData().setNotificationType(NotificationType.COMMENT_COMMENT)
                .setEnable(notificationSetting.isCommentComment()));
        dataResponse.setData(list);
        return dataResponse;
    }

    public DataResponse<SuccessResponse> changePasswd(PasswdChangeRequest passwdChangeRequest) {
        Person person = findPerson(jwtProvider.getLoginFromToken(passwdChangeRequest.getToken()));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        person.setPassword(passwordEncoder.encode(passwdChangeRequest.getPassword()));
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage("ok"));
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
