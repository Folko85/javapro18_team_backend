package com.skillbox.microservice.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    private static final String SUPPORT = "support";

    /**
     * Бин фабрики соединений.
     *
     * @return
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("rabbitmq");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        return connectionFactory;
    }

    /**
     * Бин конвертера.
     *
     * @param defaultObjectMapper
     * @return
     */
    @Bean
    MessageConverter commonJsonMessageConverter(ObjectMapper defaultObjectMapper) {
        return new Jackson2JsonMessageConverter(defaultObjectMapper);
    }

    /**
     * Бин сериализатора.
     *
     * @return
     */
    @Bean
    public ObjectMapper defaultObjectMapper() {
        final var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * Бин клиента для отправки сообщений.
     *
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(SUPPORT);
        template.setRoutingKey(SUPPORT);
        template.setMessageConverter(commonJsonMessageConverter(defaultObjectMapper()));
        return template;
    }

    /**
     * Бин администрирования очередей.
     *
     * @return
     */
    @Bean
    public AmqpAdmin amqpAdmin() {
        AmqpAdmin admin = new RabbitAdmin(connectionFactory());
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding(queue(), exchange()));
        return new RabbitAdmin(connectionFactory());
    }

    /**
     * Бин объявления очереди.
     *
     * @return
     */
    @Bean
    Queue queue() {
        return new Queue(SUPPORT);
    }

    /**
     * Бин объявления эксчейнджа.
     *
     * @return
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(SUPPORT);
    }

    /**
     * Бин привязки очереди к эксчейнджу.
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(SUPPORT);
    }
}