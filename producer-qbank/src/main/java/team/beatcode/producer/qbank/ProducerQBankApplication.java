package team.beatcode.producer.qbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProducerQBankApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerQBankApplication.class, args);
    }

}
