package com.skillbox.socialnetwork.config;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import com.skillbox.socialnetwork.api.security.JwtProvider;


import com.skillbox.socialnetwork.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Slf4j
@Configuration
public class SocketIOConfig {
    //    @Value("${ws.server.host}")
    private String HOST = "0.0.0.0";
    //    @Value("${ws.server.port}")
    private int PORT = 1111;

    private final JwtProvider jwtProvider;

    private final PersonRepository personRepository;

    private SocketIOServer server;

    public SocketIOConfig(JwtProvider jwtProvider, PersonRepository personRepository) {
        this.jwtProvider = jwtProvider;
        this.personRepository = personRepository;
    }


    @Bean
    public SocketIOServer server() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(HOST);
        config.setPort(PORT);

        config.setAuthorizationListener(data -> {
            // http://localhost:1111?token=xxxxxxx
            String token = data.getSingleUrlParam("token");
            if (token != null && jwtProvider.validateToken(token)) {
                log.info("User connect on socket {} count {}", jwtProvider.getLoginFromToken(token), (long) server.getAllClients().size());
                return personRepository.findByEMail(jwtProvider.getLoginFromToken(token)).isPresent();
            }
            return false;
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

