package com.skillbox.socialnetwork.controller;

import com.skillbox.socialnetwork.AbstractTest;
import com.skillbox.socialnetwork.NetworkApplication;
import com.skillbox.socialnetwork.api.response.tagdto.TagDto;
import com.skillbox.socialnetwork.entity.Person;
import com.skillbox.socialnetwork.entity.Tag;
import com.skillbox.socialnetwork.repository.AccountRepository;
import com.skillbox.socialnetwork.repository.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = {NetworkApplication.class})
class TagControllerTest extends AbstractTest {

    @Autowired
    private TagRepository tagRepository;

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

    @AfterEach
    public void cleanup() {
        accountRepository.deleteAll();
        tagRepository.deleteAll();

    }
    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testGetTags() throws Exception {
        tagRepository.save(new Tag().setTag("porno"));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/tags/")
                        .param("tag", "po")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].tag").value("porno"));
//                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void testPostTags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/tags/")
                        .content(mapper.writeValueAsString(new TagDto().setTag("ahaha")))   //постим задачу
                        .contentType(MediaType.APPLICATION_JSON)                          //тип на входе json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.tag").value("ahaha"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").exists());
//                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:administrate")
    void testDeleteTags() throws Exception {
        tagRepository.save(new Tag().setTag("java"));
        String id = tagRepository.findByTag("java").get().getId().toString();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/tags/")
                        .param("id", id)
                        .contentType(MediaType.APPLICATION_JSON)                          //тип на входе json
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("ok"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("nothing"));
//                .andDo(MockMvcResultHandlers.print());
    }

}