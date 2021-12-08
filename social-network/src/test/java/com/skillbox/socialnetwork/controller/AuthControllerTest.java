package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.api.request.LoginRequest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractTest {

    @Autowired
    private PersonRepository personRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @AfterEach
    public void cleanup() {
        personRepository.deleteAll();

    }

    @Test
    @DisplayName("Успешный вход в аккаунт c логином и паролем")
    void testLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        String EMAIL = "test@test.ru";
        request.setEMail(EMAIL);
        String PASSWORD = "password";
        request.setPassword(PASSWORD);
        Person person = new Person();
        person.setEMail(EMAIL);
        person.setPassword(passwordEncoder.encode(PASSWORD));
        person.setApproved(true);
        person.setDateAndTimeOfRegistration(LocalDateTime.now().minusDays(1));
        personRepository.save(person);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Попытка входа несуществующего пользователя")
    void testNotLogin() throws Exception {
        LoginRequest request = new LoginRequest();
        String EMAIL = "none@test.ru";
        request.setEMail(EMAIL);
        String PASSWORD = "password";
        request.setPassword(PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
