package com.skillbox.microservice.service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки писем.
 */
@Service
@RequiredArgsConstructor
public class SendMailService {

    @Value("${external.mail.key}")
    private String key;

    @Value("${external.mail.secret}")
    private String secret;

    @Value("${external.mail.from}")
    private String from;

    /**
     * Метод отправки сообщения.
     *
     * @param emailTo
     * @param message
     * @throws MailjetException
     * @throws JSONException
     */
    public void send(final String emailTo, final String message) throws MailjetException, JSONException {
        final MailjetClient client;
        final MailjetRequest request;
        client = new MailjetClient(ClientOptions.builder().apiKey(key).apiSecretKey(secret).build());
        request = new MailjetRequest(Emailv31.resource);
        request.property(Emailv31.MESSAGES, new JSONArray()
                .put(new JSONObject()
                        .put(Emailv31.Message.FROM, new JSONObject()
                                .put("Email", from)
                                .put("Name", "ZeroNetwork"))
                        .put(Emailv31.Message.TO, new JSONArray()
                                .put(new JSONObject()
                                        .put("Email", emailTo)
                                        .put("Name", "Dear User")))
                        .put(Emailv31.Message.SUBJECT, "Message from support Zeronetwork.")
                        .put(Emailv31.Message.TEXTPART, "Welcome to our network")
                        .put(Emailv31.Message.HTMLPART, message)
                        .put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));

        client.post(request);
    }
}
