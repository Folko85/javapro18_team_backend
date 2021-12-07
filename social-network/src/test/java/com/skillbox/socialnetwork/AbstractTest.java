package com.skillbox.socialnetwork;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.socialnetwork.repository.SessionRepository;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.binding.BindingService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class AbstractTest {
    /**
     * Web application context.
     */
    @Autowired
    protected WebApplicationContext ctx;

    /**
     * Mock mvc.
     */
    protected MockMvc mockMvc;

    @MockBean
    private AmazonSQSAsync amazonSQSAsync;

    @MockBean
    private BindingService bindingService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private QueueMessagingTemplate queueMessagingTemplate;

    @MockBean
    private SocketIOServer server;

    @MockBean
    private CacheManager cacheManager;

    /**
     * Object mapper.
     */
    @Autowired
    protected ObjectMapper mapper;

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

