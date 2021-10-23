package com.skillbox.socialnetwork.config;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.skillbox.socialnetwork.api.security.JwtProvider;
import com.skillbox.socialnetwork.repository.AccountRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;


@Configuration
public class SocketIOConfig {
    private final String SOCKET_IO_HOST = "localhost";
    private final int SOCKET_IO_PORT = 1111;
    private final JwtProvider jwtProvider;
    private final AccountRepository accountRepository;

    private SocketIOServer server;

    public SocketIOConfig(JwtProvider jwtProvider, AccountRepository accountRepository) {
        this.jwtProvider = jwtProvider;
        this.accountRepository = accountRepository;
    }


    @Bean
    public SocketIOServer server() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(SOCKET_IO_HOST);
        config.setPort(SOCKET_IO_PORT);

        config.setAuthorizationListener(data -> {
            // http://localhost:8081?token=xxxxxxx
            String token = data.getSingleUrlParam("token");

            return accountRepository.findByEMail(jwtProvider.getLoginFromToken(token)).isPresent();
        });

        server = new SocketIOServer(config);
        server.start();


        return server;
    }

    @PreDestroy
    public void stopSocketIOServer() {
        this.server.stop();
    }
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer server) {
        return new SpringAnnotationScanner(server);
    }

}

