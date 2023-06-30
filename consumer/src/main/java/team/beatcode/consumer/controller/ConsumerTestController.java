package team.beatcode.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerTestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("hello")
    public String hello() {
        return restTemplate.getForObject("http://producer-judge/helloPORT", String.class);
    }
}
