package com.skillbox.socialnetwork.service;

import com.mailjet.client.errors.MailjetException;
import com.skillbox.socialnetwork.api.request.EMailChangeRequest;
import com.skillbox.socialnetwork.api.request.NotificationsRequest;
import com.skillbox.socialnetwork.api.request.PasswdChangeRequest;
import com.skillbox.socialnetwork.api.request.RecoveryRequest;
import com.skillbox.socialnetwork.api.request.RegisterRequest;
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
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

/**
 * Сервис учётных записей.
 */
@Service
@AllArgsConstructor
public class AccountService {

    private static final String OK = "ok";
    private static final String DASH = "-";
    private static final Integer FOUR = 4;

    private final PersonRepository personRepository;
    private final MailSender mailSender;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationProperties registrationProperties;
    private final NotificationSettingRepository notificationSettingRepository;

    /**
     * Регистрация.
     * @param registerRequest
     * @return
     * @throws UserExistException
     * @throws MailjetException
     */
    public DataResponse<SuccessResponse> register(RegisterRequest registerRequest) throws UserExistException, MailjetException {
        if (personRepository.findByEMail(registerRequest.getEMail()).isPresent()) {
            throw new UserExistException();
        }
        Person person = new Person();
        person.setEMail(registerRequest.getEMail());
        person.setFirstName(registerRequest.getFirstName());
        person.setLastName(registerRequest.getLastName());
        person.setConfirmationCode(registerRequest.getCode());
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword1()));
        person.setDateAndTimeOfRegistration(LocalDateTime.now(ZoneOffset.UTC));
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setLastOnlineTime(ZonedDateTime.now(ZoneOffset.UTC).toLocalDateTime());
        String code = UUID.randomUUID().toString().replace(DASH, "");
        if (registrationProperties.isNeed()) {
            mailSender.send(registerRequest.getEMail(), registrationProperties.getUrl() + "?key=" + code + "&eMail=" + registerRequest.getEMail());
            person.setConfirmationCode(code);
        } else {
            person.setApproved(true);
        }
        person.setRole(Role.USER);
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage(OK));

    }

    /**
     * Выслать сообщение восстановления.
     *
     * @param recoveryRequest
     * @return
     * @throws MailjetException
     */
    public String sendRecoveryMessage(RecoveryRequest recoveryRequest) throws MailjetException {
        Person person = findPerson(recoveryRequest.getEMail());
        String code = UUID.randomUUID().toString().replace(DASH, "").substring(0, FOUR);
        person.setConfirmationCode(code);
        personRepository.save(person);

        mailSender.send(recoveryRequest.getEMail(), "Enter this code:" + code);
        return "Код подтверждения выслан";
    }

    /**
     * Восстановление завершено.
     *
     * @param key
     * @param eMail
     * @return
     * @throws MailjetException
     */
    public String recoveryComplete(String key, String eMail) throws MailjetException {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            String passwd = UUID.randomUUID().toString().replace(DASH, "");
            person.setPassword(passwordEncoder.encode(passwd));
            person.setConfirmationCode("");
            mailSender.send(eMail, passwd);
            personRepository.save(person);
        } else {
            throw new EntityNotFoundException("");
        }
        return "Новый пароль выслан";
    }

    /**
     * Завершение регистрации.
     *
     * @param key
     * @param eMail
     * @return
     */
    public String registrationComplete(String key, String eMail) {
        Person person = findPerson(eMail);
        if (person.getConfirmationCode().equals(key)) {
            person.setApproved(true);
            person.setConfirmationCode("");
            personRepository.save(person);
        } else {
            throw new EntityNotFoundException("");
        }
        return "Аккаунт подтверждён";
    }

    /**
     * Изменение почты.
     *
     * @param eMailChangeRequest
     * @param principal
     * @return
     * @throws UserExistException
     */
    public DataResponse<SuccessResponse> changeEMail(EMailChangeRequest eMailChangeRequest, Principal principal) throws UserExistException {
        if (personRepository.findByEMail(eMailChangeRequest.getEMail()).isPresent()) {
            throw new UserExistException();
        }
        Person person = findPerson(principal.getName());
        person.setEMail(eMailChangeRequest.getEMail());
        SecurityContextHolder.clearContext();
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage(OK));

    }

    /**
     * Получение настроек уведомлений.
     *
     * @param notificationsRequest
     * @param principal
     * @return
     */
    public DataResponse<SuccessResponse> setNotificationsSetting(NotificationsRequest notificationsRequest, Principal principal) {
        Person person = findPerson(principal.getName());
        NotificationSetting notificationSetting = notificationSettingRepository.findNotificationSettingByPersonId(person.getId())
                .orElse(new NotificationSetting().setFriendsRequest(true).setCommentComment(true).setPostComment(true).setPerson(person));
        switch (notificationsRequest.getNotificationType()) {
            case FRIEND_REQUEST -> notificationSetting.setFriendsRequest(notificationsRequest.isEnable());
            case POST_COMMENT -> notificationSetting.setPostComment(notificationsRequest.isEnable());
            case COMMENT_COMMENT -> notificationSetting.setCommentComment(notificationsRequest.isEnable());
            default -> throw new UnableToSendNotificationException("Неизвестный тип уведомления");
        }
        notificationSettingRepository.save(notificationSetting);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage(OK));
    }

    /**
     * Получить настройки уведослений.
     *
     * @param principal
     * @return
     */
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

    /**
     * Изменить пароль.
     *
     * @param passwdChangeRequest
     * @return
     */
    public DataResponse<SuccessResponse> changePasswd(PasswdChangeRequest passwdChangeRequest) {
        Person person = findPerson(jwtProvider.getLoginFromToken(passwdChangeRequest.getToken()));
        person.setPassword(passwordEncoder.encode(passwdChangeRequest.getPassword()));
        personRepository.save(person);
        return new DataResponse<SuccessResponse>().setTimestamp(Instant.now()).setData(new SuccessResponse().setMessage(OK));
    }

    private Person findPerson(String eMail) {
        return personRepository.findByEMail(eMail)
                .orElseThrow(() -> new UsernameNotFoundException(eMail));
    }

}
