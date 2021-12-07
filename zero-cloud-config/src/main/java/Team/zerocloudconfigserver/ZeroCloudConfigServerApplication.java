package Team.zerocloudconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ZeroCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroCloudConfigServerApplication.class, args);
    }

}
