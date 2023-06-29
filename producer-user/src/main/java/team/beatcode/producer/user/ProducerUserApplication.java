package team.beatcode.producer.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProducerUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerUserApplication.class, args);
    }

}
