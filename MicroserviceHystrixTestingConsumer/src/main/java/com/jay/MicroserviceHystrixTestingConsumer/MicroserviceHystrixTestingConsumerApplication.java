package com.jay.MicroserviceHystrixTestingConsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class MicroserviceHystrixTestingConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceHystrixTestingConsumerApplication.class, args);
	}

}
