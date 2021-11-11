package com.skillbox.microservice.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@EnableScheduling
@Configuration
public class SqsConfig {

//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secretKey;
//    @Value("${cloud.aws.sqs.endpoint}")
//    private String endpoint;
//    @Value("${cloud.yandex.region}")
//    private String region;

    @Bean
    @Primary
    //Bean для работы с Yandex Message Queue
    public AmazonSQSAsync amazonSQSAsync() throws ExecutionException, InterruptedException, TimeoutException {
        return AmazonSQSAsyncClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("bjwgIspnfqIQsSEvsivr", "ct15ZUAOTO0ARY-_bCB4QmlR2IREqDpbGgMN7Z1b")))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://message-queue.api.cloud.yandex.net/", "ru-central1"))
                .build();
    }

    @Bean
    @Primary
    //Bean для отправки сообщений
    public QueueMessagingTemplate queueMessagingTemplate() throws ExecutionException, InterruptedException, TimeoutException {
        return new QueueMessagingTemplate(amazonSQSAsync());
    }

//    @Value("${cloud.aws.credentials.access-key}")
//    private String accessKey;
//    @Value("${cloud.aws.credentials.secret-key}")
//    private String secret;
//    @Value("${cloud.aws.s3.endpoint}")
//    private String endpoint;
//    @Value("${cloud.aws.s3.region}")
//    private String region;

//    @Bean
//    @Primary
//    public AmazonSQSAsync amazonSQSAsync() {
//        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder.standard()
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("bjwgIspnfqIQsSEvsivr", "ct15ZUAOTO0ARY-_bCB4QmlR2IREqDpbGgMN7Z1b")))
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://message-queue.api.cloud.yandex.net/", "ru-central1"))
//                .build();
//        amazonSQSAsync.createQueueAsync("queueFromJavaCode");
//        return amazonSQSAsync;
//    }
//
//    @Bean
//    @Primary
//    public QueueMessagingTemplate queueMessagingTemplate() {
//        return new QueueMessagingTemplate(amazonSQSAsync());
//    }
}
