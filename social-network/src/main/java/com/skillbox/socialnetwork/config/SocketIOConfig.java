package com.skillbox.socialnetwork.config;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
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

    private SocketIOServer server;


    @Bean
    public SocketIOServer server() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(HOST);
        config.setPort(PORT);

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

