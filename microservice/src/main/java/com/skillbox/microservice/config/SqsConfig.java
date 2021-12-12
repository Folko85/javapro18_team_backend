package com.skillbox.microservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Configuration
public class SqsConfig {
    private final int QUEUE_CREATE_DELAY = 10;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secret;
    @Value("${cloud.aws.sqs.endpoint}")
    private String endpoint;
    @Value("${cloud.yandex.region}")
    private String region;
    @Value("${message.queue.incoming}")
    private String queueName;

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() throws ExecutionException, InterruptedException, TimeoutException {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secret)))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
        amazonSQSAsync.createQueueAsync(queueName).get(QUEUE_CREATE_DELAY, TimeUnit.SECONDS);
        return amazonSQSAsync;
    }

    @Bean
    @Primary
    public QueueMessagingTemplate queueMessagingTemplate() throws ExecutionException, InterruptedException, TimeoutException {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }
}
