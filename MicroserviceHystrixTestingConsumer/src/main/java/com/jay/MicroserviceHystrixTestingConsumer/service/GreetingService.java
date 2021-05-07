package com.jay.MicroserviceHystrixTestingConsumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GreetingService {
    @HystrixCommand(fallbackMethod = "defaultGreeting")
    public String getGreeting(String name){
        return new RestTemplate().getForObject("http://localhost:9090/greet/{name}",String.class, name);
    }

    private String defaultGreeting(String name){
        return "Hello User";
    }

}
