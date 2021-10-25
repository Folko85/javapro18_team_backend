package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
        Person ivan = new Person();
        ivan.setEMail("ivanov@test.ru");
        ivan.setPassword("password");

        Person petr = new Person();
        petr.setEMail("petrov@test.ru");
        petr.setPassword("password");

        accountRepository.save(ivan);
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