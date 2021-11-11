package com.skillbox.socialnetwork.controller;


import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.SessionTemplate;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class SocketEventHandler {
    private final SocketIOServer server;
    private final SessionTemplate template;
    private final PersonRepository accountRepository;
    private final JwtProvider jwtProvider;

    public SocketEventHandler(SocketIOServer server,
                              SessionTemplate template,
                              PersonRepository accountRepository,
                              JwtProvider jwtProvider) {
        this.server = server;
        this.template = template;
        this.accountRepository = accountRepository;
        this.jwtProvider = jwtProvider;

    }


    @OnConnect
    public void onConnect(SocketIOClient client) {
        if (client != null) {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            UUID sessionId = client.getSessionId();
            accountRepository.findByEMail(jwtProvider.getLoginFromToken(token))
                    .ifPresent(person -> template.save(person.getId(), sessionId));
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client != null) {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            accountRepository.findByEMail(jwtProvider.getLoginFromToken(token))
                    .ifPresent(person -> template.deleteByUserId(person.getId()));

            client.disconnect();
        }
    }

//
//    @OnEvent(value = "CHAT")
//    public void onChatEvent(SocketIOClient client, AckRequest request, MessageRequest data) {
//        server.getAllClients().forEach(socketIOClient -> socketIOClient.sendEvent("message", data));
//
//    }


}
