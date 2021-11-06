package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = {NetworkApplication.class})
public class PostControllerTest extends AbstractTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository accountRepository;

    Person person;

    @BeforeEach
    public void setup() {
        super.setup();
        person = new Person();
        person.setEMail("test@test.ru");
        person.setPassword("password");
        person.setDateAndTimeOfRegistration(LocalDateTime.now().minusDays(5));
        person.setLastOnlineTime(LocalDateTime.now().minusDays(3));
        accountRepository.save(person);
    }

    @AfterEach
    public void cleanup() {
        accountRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testGetPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post")
                        .principal(() -> "test@test.ru")
                        .param("testText", "hi bro")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setPerson(person);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testPutPost() throws Exception {
        String json = "{\"title\": \"1\", \"post_text\": \"1\"}";
        Post post = new Post();
        post.setPerson(person);
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(json)
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testDeletePost() throws Exception {
        Post post = new Post();
        post.setPerson(person);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testRecoverPost() throws Exception {
        Post post = new Post();
        post.setPerson(person);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/{id}/recover", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
