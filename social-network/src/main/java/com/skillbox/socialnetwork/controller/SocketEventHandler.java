package com.skillbox.socialnetwork.controller;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.skillbox.socialnetwork.api.request.socketio.AuthRequest;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.repository.PersonRepository;
import com.skillbox.socialnetwork.repository.SessionTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
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
        //            String token = client.getHandshakeData().getSingleUrlParam("token");
        //            UUID sessionId = client.getSessionId();
        //            accountRepository.findByEMail(jwtProvider.getLoginFromToken(token))
        //                    .ifPresent(person -> template.save(person.getId(), sessionId));
        //            log.info("User connect on socket {} count {}",jwtProvider.getLoginFromToken(token), (long) server.getAllClients().size());

    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client != null) {
            String token = client.getHandshakeData().getSingleUrlParam("token");
            accountRepository.findByEMail(jwtProvider.getLoginFromToken(token))
                    .ifPresent(person -> template.deleteByUserId(person.getId()));
            client.disconnect();
            log.info("User disconnect on socket {} count {}",jwtProvider.getLoginFromToken(token), (long) server.getAllClients().size());
        }
    }


    @OnEvent(value = "auth")
    public void onAuthEvent(SocketIOClient client, AckRequest request, AuthRequest data) {
                    String token = data.getToken();
                    UUID sessionId = client.getSessionId();
                    accountRepository.findByEMail(jwtProvider.getLoginFromToken(token))
                            .ifPresent(person -> template.save(person.getId(), sessionId));
                    log.info("User authorize on socket {} count {}",jwtProvider.getLoginFromToken(token), (long) server.getAllClients().size());
    }


}
