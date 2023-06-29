package team.beatcode.producer.judge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProducerJudgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerJudgeApplication.class, args);
    }

}
