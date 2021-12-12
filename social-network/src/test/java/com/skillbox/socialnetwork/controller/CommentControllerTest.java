package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.api.request.CommentRequest;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Post;
import com.skillbox.socialnetwork.entity.PostComment;
import com.skillbox.socialnetwork.entity.enums.Role;
import com.skillbox.socialnetwork.repository.CommentRepository;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {NetworkApplication.class})
@AutoConfigureMockMvc
class CommentControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    private Person person;

    private Post post;

    private PostComment postComment;


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

        post = new Post();
        post.setPerson(person);
        postRepository.save(post);

        postComment = new PostComment();
        postComment.setPerson(person);
        postComment.setCommentText("ABOBA");
        postComment.setTime(LocalDateTime.now());
        postComment.setPost(post);
        commentRepository.save(postComment);
    }


    @AfterEach
    public void cleanup() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void postComment() throws Exception {
        Integer postId = post.getId();
        CommentRequest request = new CommentRequest();
        request.setParentId(postComment.getId());
        request.setCommentText("ABOBAB");

        this.mockMvc.perform(post("/api/v1/post/{id}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void putComment() throws Exception {
        Integer postId = post.getId();
        String commentId = postComment.getId().toString();
        CommentRequest request = new CommentRequest();
        request.setParentId(postComment.getId());
        request.setCommentText("ABOBAB");

        this.mockMvc.perform(put("/api/v1/post/{id}/comments/{comment_id}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void deleteComment() throws Exception {
        String postId = post.getId().toString();
        String commentID = postComment.getId().toString();
        this.mockMvc.perform(delete("/api/v1/post/{id}/comments/{comment_id}", postId, commentID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void recoveryComment() throws Exception {
        String postId = post.getId().toString();
        String commentID = postComment.getId().toString();
        postComment.setDeleted(true);
        this.mockMvc.perform(put("/api/v1/post/{id}/comments/{comment_id}/recover", postId, commentID))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void getFeeds() throws Exception {
        String postId = post.getId().toString();
        this.mockMvc.perform(get("/api/v1/post/{id}/comments", postId)
                        .param("offset", "0")
                        .param("itemPerPage", "5"))
                .andExpect(status().isOk());
    }
}