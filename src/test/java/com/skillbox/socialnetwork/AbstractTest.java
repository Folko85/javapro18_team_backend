package com.skillbox.socialnetwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@ExtendWith(SpringExtension.class)
public abstract class AbstractTest {
    /** Web application context. */
    @Autowired
    protected WebApplicationContext ctx;

    /** Mock mvc. */
    protected MockMvc mockMvc;

    /** Object mapper. */
    @Autowired
    protected ObjectMapper mapper; // это нам пригодится в следующем ДЗ для проверки более сложного API

    /**
     * Create mock mvc.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .build();
    }
}

