package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.LikeRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
class LikeControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    LikeRepository likeRepository;

    @BeforeEach
    public void setup() {
        super.setup();
        Person person = new Person();
        person.setEMail("test@test.ru");
        person.setPassword("password");
        person = accountRepository.save(person);
    }

    @AfterEach
    public void cleanup() {
        likeRepository.deleteAll();
        postRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void putLikes() throws Exception {
        Post post = postRepository.save(new Post());
        String json = "{\"item_id\": " + post.getId() + ", \"type\": \"Post\"}";

        this.mockMvc.perform(put("/api/v1//likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void deleteLikes() throws Exception {
        Post post = postRepository.save(new Post());
        String id = String.valueOf(post.getId());
        String json = "{\"item_id\": " + post.getId() + ", \"type\": \"Post\"}";
        mockMvc.perform(put("/api/v1//likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        this.mockMvc.perform(delete("/api/v1//likes")
                .param("item_id", id)
                .param("type", "Post"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void getLikes() throws Exception {
        Post post = postRepository.save(new Post());
        String id = String.valueOf(post.getId());
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1//likes")
                .param("item_id", id)
                .param("type", "Post"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}