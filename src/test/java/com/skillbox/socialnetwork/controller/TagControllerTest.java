package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {NetworkApplication.class})
public class TagControllerTest extends AbstractTest {

    @Autowired
    private TagRepository repository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    public void setup() {
        super.setup();
        Person person = new Person();
        person.setEMail("test@test.ru");
        person.setPassword("password");
        accountRepository.save(person);
    }

    @Test
    @WithMockUser (username = "test@test.ru", authorities = "user:write")
    public void testGetTags() throws Exception {
        repository.save(new Tag().setTag("порно"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/tags/")
                        .param("tag", "по")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}