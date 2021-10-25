package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
class FriendshipControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        super.setup();
        Person oleg = new Person();
        oleg.setId(1);
        oleg.setFirstName("Олег");
        oleg.setLastName("Иванов");
        oleg.setDateAndTimeOfRegistration(LocalDateTime.now());
        oleg.setEMail("ivanov@test.ru");
        oleg.setPassword("password");
        oleg.setApproved(true);
        oleg.setLastOnlineTime(LocalDateTime.now());
        oleg.setBlocked(false);

        Person petr = new Person();
        petr.setId(2);
        petr.setFirstName("Петр");
        petr.setLastName("Петров");
        petr.setDateAndTimeOfRegistration(LocalDateTime.now());
        petr.setEMail("petrov@test.ru");
        petr.setPassword("password");
        petr.setApproved(true);
        petr.setLastOnlineTime(LocalDateTime.now());
        petr.setBlocked(false);

        accountRepository.save(oleg);
        accountRepository.save(petr);

    }

//    @AfterEach
//    public void resetDb() {
//        accountRepository.deleteAll();
//    }

    @Test
    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
    void findFriend() throws Exception {
        mockMvc.perform(get("/api/v1/friends", "", "0", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
    void stopBeingFriends() throws Exception {
        mockMvc.perform(delete("/api/v1/friends/{id}", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
    void addingToFriends() throws Exception {
        mockMvc.perform(post("/api/v1/friends/{id}", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
    void listApplications() throws Exception {
        mockMvc.perform(get("/api/v1/friends/request", "", "0", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
    void getRecommendedUsers() throws Exception {
        mockMvc.perform(get("/api/v1/friends/request", "0", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser(username = "ivanov@test.ru", authorities = "user:write")
//    void isFriends() throws Exception {
//        mockMvc.perform(post("/api/v1/is/friends")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
}