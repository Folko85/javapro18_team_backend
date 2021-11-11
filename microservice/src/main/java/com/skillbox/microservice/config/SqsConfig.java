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

@Configuration
public class SqsConfig {

//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secret;
//    @Value("${cloud.aws.s3.endpoint}")
//    private String endpoint;
//    @Value("${cloud.aws.s3.region}")
//    private String region;

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("bjwgIspnfqIQsSEvsivr", "ct15ZUAOTO0ARY-_bCB4QmlR2IREqDpbGgMN7Z1b")))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://message-queue.api.cloud.yandex.net/", "ru-central1"))
                .build();
        amazonSQSAsync.createQueueAsync("queueFromJavaCode");
        return amazonSQSAsync;
    }

    @Bean
    @Primary
    public QueueMessagingTemplate queueMessagingTemplate() {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }
}
