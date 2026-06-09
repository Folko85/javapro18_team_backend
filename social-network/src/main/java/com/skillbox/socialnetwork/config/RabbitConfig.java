package com.skillbox.socialnetwork.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.skillbox.socialnetwork.config.property.RabbitProperties;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RabbitConfig {

    private final RabbitProperties properties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory(properties.getHost());
        connectionFactory.setPort(properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        return connectionFactory;
    }

    /**
     * Бин конвертера.
     * @param defaultObjectMapper
     * @return
     */
    @Bean
    MessageConverter commonJsonMessageConverter(ObjectMapper defaultObjectMapper) {
        return new Jackson2JsonMessageConverter(defaultObjectMapper);
    }

    /**
     * Стандартный сериализатор.
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
     * Бин административных функций.
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
     * Бин клиента для Рэббита.
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(properties.getExchange());
        template.setRoutingKey(properties.getRoutingKey());
        template.setMessageConverter(commonJsonMessageConverter(defaultObjectMapper()));
        return template;
    }


    /**
     * Бин очереди.
     * @return
     */
    @Bean
    Queue queue() {
        return new Queue(properties.getQueue());
    }

    /**
     * Бин эксчейнджа.
     * @return
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.getExchange());
    }

    /**
     * Бин связи эксчейнджа и очереди.
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(properties.getRoutingKey());
    }
}