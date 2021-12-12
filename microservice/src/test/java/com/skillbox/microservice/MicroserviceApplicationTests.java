package com.skillbox.microservice;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MicroserviceApplicationTests {

    @MockBean
    private AmqpAdmin amqpAdmin;

    @Test
    void contextLoads() {
    }

}
