package com.jay.MicroserviceHystrixTesting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingControllerImpl implements GreetingController{

    @Override
    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name) {
        return "hello" + name;
    }

}
