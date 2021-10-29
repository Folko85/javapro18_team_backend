package com.skillbox.socialnetwork.service;

import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.api.response.AccountResponse;
import com.skillbox.socialnetwork.api.response.DataResponse;
import com.skillbox.socialnetwork.api.response.authdto.AuthData;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.api.security.UserDetailServiceImpl;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.exception.DeletedAccountLoginException;
import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static com.skillbox.socialnetwork.service.AccountService.getAccountResponse;
import static com.skillbox.socialnetwork.service.UserService.deletedImage;
import static java.time.ZoneOffset.UTC;

@Slf4j
@Service
public class AuthService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserDetailServiceImpl userDetailService;

    public AuthService(PersonRepository accountRepository,
                       PasswordEncoder passwordEncoder,
                       JwtProvider jwtProvider,
                       UserDetailServiceImpl userDetailService) {
        this.personRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.userDetailService = userDetailService;
    }


    public DataResponse auth(LoginRequest loginRequest) throws DeletedAccountLoginException {
        UserDetails userDetails = userDetailService.loadUserByUsername(loginRequest.getEMail());
        String token;
        Person person = personRepository.findByEMail(loginRequest.getEMail())
                .orElseThrow(() -> new UsernameNotFoundException(loginRequest.getEMail()));
        if (person.isDeleted()) {
            log.error("Deleted User with email " + person.getEMail() + "tries to login");
            throw new DeletedAccountLoginException();
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            token = jwtProvider.generateToken(loginRequest.getEMail());
        } else throw new UsernameNotFoundException(loginRequest.getEMail());

        DataResponse authResponse = new DataResponse();
        authResponse.setTimestamp(ZonedDateTime.now().toInstant());
        AuthData authData;
        authData = setAuthData(person);
        authData.setToken(token);
        authResponse.setData(authData);
        return authResponse;
    }


    public AccountResponse logout() {
        SecurityContextHolder.clearContext();
        return getAccountResponse(UTC);

    }

    static AuthData setAuthData(Person person) {
        AuthData authData = new AuthData();
        authData.setEMail(person.getEMail());
        authData.setAbout(person.getAbout());
        if (person.getBirthday() != null)
            authData.setBirthDate(person.getBirthday().atStartOfDay().toInstant(UTC));
        if (person.getCountry() != null)
            authData.setCountry(person.getCountry());
        if (person.getCity() != null)
            authData.setCity(person.getCity());
        authData.setFirstName(person.getFirstName());
        authData.setLastName(person.getLastName());
        authData.setId(person.getId());
        authData.setRegDate(person.getDateAndTimeOfRegistration().toInstant(UTC));
        authData.setPhone(person.getPhone());
        authData.setMessagesPermission(person.getMessagesPermission());
        authData.setBlocked(person.isBlocked());
        authData.setPhoto(person.getPhoto());
        if (person.getLastOnlineTime() != null)
            authData.setLastOnlineTime(person.getLastOnlineTime().toInstant(ZoneOffset.UTC));
        return authData;
    }

    static AuthData setBlockerAuthData(Person person) {
        AuthData authData = new AuthData();
        authData.setId(person.getId());
        authData.setFirstName(person.getFirstName());
        authData.setLastName(person.getLastName());
        return authData;
    }

    static AuthData setDeletedAuthData(Person person) {
        AuthData authData = setBlockerAuthData(person);
        authData.setAbout("Страница удалена");
        authData.setPhoto(deletedImage);
        return authData;
    }

}
