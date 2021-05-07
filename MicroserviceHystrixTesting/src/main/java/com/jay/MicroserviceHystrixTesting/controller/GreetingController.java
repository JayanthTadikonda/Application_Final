package com.jay.MicroserviceHystrixTesting.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface GreetingController {

    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name);
}
