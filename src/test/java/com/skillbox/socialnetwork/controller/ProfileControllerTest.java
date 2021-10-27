package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
class ProfileControllerTest extends AbstractTest {


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
        person.setEMail("jma@test.ru");
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
    @WithMockUser(username = "jma@test.ru", authorities = "user:write")
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
//                .principal(new Principal() {
//                    @Override
//                    public String getName() {
//                        return "jma";
//                    }
//                }))
                .andDo(print())
                .andExpect(status().isOk());
    }
}