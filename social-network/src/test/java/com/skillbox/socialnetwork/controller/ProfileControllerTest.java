package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    Person person;


    @BeforeEach
    public void setup() {
        super.setup();
        person = new Person();
        person.setPassword("password");
        person.setFirstName("Valera");
        person.setLastName("Jma");
        person.setEMail("test@test.ru");
        person.setPassword("password");
        person.setBirthday(LocalDate.of(1988, 1, 5));
        person.setRole(Role.USER);
        person.setApproved(true);
        person.setDateAndTimeOfRegistration(LocalDateTime.now());
        person.setLastOnlineTime(LocalDateTime.now());
        person.setBlocked(false);
        personRepository.save(person);
    }

    @AfterEach
    public void cleanup() {
        personRepository.deleteAll();
    }


    @Test
    @DisplayName("Поиск пользователей")
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void search() throws Exception {

        this.mockMvc.perform(get("/api/v1/users/search")
                .param("first_name", person.getFirstName())
                .param("last_name", person.getLastName())
                .param("age_from", "")
                .param("age_to", "")
                .param("country_id", "")
                .param("city_id", "")
                .param("offset", "")
                .param("itemPerPage", "20"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Попытка входа незарегистрированного пользователя")
    @WithMockUser(username = "not@test.ru", authorities = "user:write")
    void testGetMe() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error_description").value("Пользователь не существует"));
    }
}