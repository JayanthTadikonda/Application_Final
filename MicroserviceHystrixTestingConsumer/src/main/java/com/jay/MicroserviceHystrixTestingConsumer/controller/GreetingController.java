package com.jay.MicroserviceHystrixTestingConsumer.controller;

import com.jay.MicroserviceHystrixTestingConsumer.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GreetingController {

    @Autowired
    GreetingService greetingService;

    @GetMapping("/greet/{name}")
    public String getGreeting(Model model, @PathVariable String name){
        model.addAttribute("greeting", greetingService.getGreeting(name));
        return "greeting-view";
    }
}
