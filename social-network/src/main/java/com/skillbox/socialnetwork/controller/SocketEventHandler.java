package com.skillbox.socialnetwork.controller;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.skillbox.socialnetwork.api.request.socketio.AuthRequest;
import com.skillbox.socialnetwork.api.request.socketio.ReadMessagesData;
import com.skillbox.socialnetwork.api.request.socketio.TypingData;
import com.skillbox.socialnetwork.repository.SessionTemplate;
import com.skillbox.socialnetwork.service.AuthService;
import com.skillbox.socialnetwork.service.DialogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class SocketEventHandler {
    private final SocketIOServer server;
    private final SessionTemplate template;
    private final AuthService authService;
    private final DialogService dialogService;

    public SocketEventHandler(SocketIOServer server,
                              SessionTemplate template,
                              AuthService authService,
                              DialogService dialogService) {
        this.server = server;
        this.template = template;
        this.authService = authService;
        this.dialogService = dialogService;
    }


    @OnConnect
    public void onConnect(SocketIOClient client) {

        log.info("User connect on socket user {} count {}", client.getSessionId(), (long) server.getAllClients().size());
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        if (client != null) {
            Optional<Integer> id = template.findByUserUUID(client.getSessionId());
            if (id.isPresent()) {
                template.deleteByUserId(id.get());
                client.disconnect();
                log.info("User disconnect on socket count {}", (long) server.getAllClients().size());
            }
        }
    }

    @OnEvent(value = "newListener")
    public void onNewListenerEvent(SocketIOClient client) {
        log.info("User listen on socket");
        if (client != null) {
            if (template.findByUserUUID(client.getSessionId()).isPresent()) {
                client.sendEvent("auth-response", "ok");
            } else client.sendEvent("auth-response", "not");
        }
    }

    @OnEvent(value = "auth")
    public void onAuthEvent(SocketIOClient client, AckRequest request, AuthRequest data) {
        if (client != null) {
            authService.socketAuth(data, client.getSessionId());
        }
    }

    @OnEvent(value = "start-typing")
    public void onStartTypingEvent(SocketIOClient client, AckRequest request, TypingData data) {
        if (client != null) {
            dialogService.startTyping(data);
        }
    }

    @OnEvent(value = "stop-typing")
    public void onStopTypingEvent(SocketIOClient client, AckRequest request, TypingData data) {
        if (client != null) {
            template.findByUserUUID(client.getSessionId()).ifPresent(id -> dialogService.stopTyping(data));

        }
    }

    @OnEvent(value = "read-messages")
    public void onReadMessagesEvent(SocketIOClient client, AckRequest request, ReadMessagesData data) {
        if (client != null) {
            template.findByUserUUID(client.getSessionId()).ifPresent(id -> dialogService.readMessage(data, id));
        }
    }
}
