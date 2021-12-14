package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.api.request.PostRequest;
import com.skillbox.socialnetwork.entity.*;
import com.skillbox.socialnetwork.entity.enums.FriendshipStatusCode;
import com.skillbox.socialnetwork.repository.*;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(classes = {NetworkApplication.class})
public class PostControllerTest extends AbstractTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    FriendshipStatusRepository friendshipStatusRepository;

    @Autowired
    FriendshipRepository friendshipRepository;

    Person owner;

    @BeforeEach
    public void setup() {
        super.setup();
        owner = getPerson("user", "test@test.ru", "password");
    }

    @AfterEach
    public void cleanup() {
        friendshipRepository.deleteAll();
        friendshipStatusRepository.deleteAll();
        personRepository.deleteAll();
        postRepository.deleteAll();
        tagRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testSearchPosts() throws Exception {
        Tag tag = tagRepository.save(new Tag().setTag("tag"));
        tagRepository.save(new Tag().setTag("tag2"));

        Post post = new Post();
        post.setTitle("Title");
        post.setPostText("Some Text. hi bro");
        post.setTags(Set.of(tag));
        post.setDatetime(Instant.now());
        post.setPerson(getPerson("newUser", "test2@test.ru", "password"));
        postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post")
                        .principal(() -> "test@test.ru")
                        .param("author", "newUser")
                        .param("tag", "tag|tag2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testGetFeeds() throws Exception {
        Post post = new Post();
        post.setTitle("Title");
        post.setPostText("Some Text. hi bro");
        post.setDatetime(Instant.now());
        Person test2 = getPerson("newUser", "test2@test.ru", "password");
        post.setPerson(test2);
        postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/feeds")
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(0));

        FriendshipStatus friendshipStatus = new FriendshipStatus();
        friendshipStatus.setCode(FriendshipStatusCode.FRIEND);
        FriendshipStatus saved = friendshipStatusRepository.save(friendshipStatus);
        Friendship friendship = new Friendship();
        friendship.setSrcPerson(owner);
        friendship.setDstPerson(test2);
        friendship.setStatus(saved);
        friendshipRepository.save(friendship);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/feeds")
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1));

    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testPostWall() throws Exception {
        PostRequest request = new PostRequest();
        request.setTitle("Title");
        request.setPostText("some text");
        Tag tag = new Tag().setTag("tag");
        tagRepository.save(tag);
        request.setTags(List.of("tag"));
        int id = personRepository.findByEMail("test@test.ru").get().getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users/{id}/wall", id)
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{id}/wall", id)
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].tags[0]").value("tag"));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setPerson(owner);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testPutPost() throws Exception {
        String json = "{\"title\": \"1\", \"post_text\": \"1\"}";
        Post post = new Post();
        post.setPerson(owner);
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(json)
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testDeletePost() throws Exception {
        Post post = new Post();
        post.setPerson(owner);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testRecoverPost() throws Exception {
        Post post = new Post();
        post.setPerson(owner);
        post.setDatetime(LocalDateTime.now().minusDays(3).toInstant(ZoneOffset.UTC));
        postRepository.save(post);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/post/{id}", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/{id}/recover", post.getId())
                        .principal(() -> "test@test.ru")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private Person getPerson(String firstName, String email, String password) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setEMail(email);
        person.setPassword(password);
        person.setDateAndTimeOfRegistration(LocalDateTime.now().minusDays(5));
        person.setLastOnlineTime(LocalDateTime.now().minusDays(3));
        return personRepository.save(person);

    }

}
