package com.skillbox.socialnetwork.controller;


import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {NetworkApplication.class})
public class TagControllerTest extends AbstractTest {

    @Autowired
    TagRepository repository;

    @Test
    public void testGetTags() throws Exception {
        repository.save(new Tag().setTag("порно"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/tags/")
                        .param("tag", "по")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}