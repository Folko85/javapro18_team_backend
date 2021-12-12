package com.skillbox.socialnetwork.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory =
                new CachingConnectionFactory("rabbitmq");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        AmqpAdmin admin = new RabbitAdmin(connectionFactory());
        admin.declareExchange(exchange());
        admin.declareQueue(queue());
        admin.declareBinding(binding(queue(), exchange()));
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange("support");
        template.setRoutingKey("support");
        return template;
    }

    @Bean
    Queue queue() {
        return new Queue("support");
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange("support");
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("support");
    }
}