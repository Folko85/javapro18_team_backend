package Team.zerocloudconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableConfigServer
public class ZeroCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZeroCloudConfigServerApplication.class, args);
    }

}
