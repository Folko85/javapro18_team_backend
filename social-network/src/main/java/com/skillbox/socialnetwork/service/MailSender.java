package com.skillbox.socialnetwork.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import com.skillbox.socialnetwork.config.property.MailProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MailSender {

    private final MailProperties mailProperties;

    public void send(String emailTo, String message) throws MailjetException {
        MailjetClient client;
        MailjetRequest request;
        MailjetResponse response;
        client = new MailjetClient(ClientOptions.builder().apiKey(mailProperties.getKey()).apiSecretKey(mailProperties.getSecret()).build());
        request = new MailjetRequest(Emailv31.resource)
                .property(Emailv31.MESSAGES, new JSONArray()
                        .put(new JSONObject()
                                .put(Emailv31.Message.FROM, new JSONObject()
                                        .put("Email", mailProperties.getFrom())
                                        .put("Name", "ZeroNetwork"))
                                .put(Emailv31.Message.TO, new JSONArray()
                                        .put(new JSONObject()
                                                .put("Email", emailTo)
                                                .put("Name", "Dear User")))
                                .put(Emailv31.Message.SUBJECT, "Greetings from Zeronetwork.")
                                .put(Emailv31.Message.TEXTPART, "Добро пожаловать в нашу соцсеть!!!")
                                .put(Emailv31.Message.HTMLPART, message)
                                .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
        response = client.post(request);
        log.info(String.valueOf(response.getStatus()));
        log.info(String.valueOf(response.getData()));
    }
}