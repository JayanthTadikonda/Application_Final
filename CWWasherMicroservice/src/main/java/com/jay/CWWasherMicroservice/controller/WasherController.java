package com.jay.CWWasherMicroservice.controller;

import com.jay.CWWasherMicroservice.filter.JwtFilter;
import com.jay.CWWasherMicroservice.model.AuthRequest;
import com.jay.CWWasherMicroservice.model.RatingReview;
import com.jay.CWWasherMicroservice.model.Washer;
import com.jay.CWWasherMicroservice.repository.WasherRepository;
import com.jay.CWWasherMicroservice.service.WasherService;
import org.springframework.security.authentication.AuthenticationManager;
import com.jay.CWWasherMicroservice.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/washer")
@SuppressWarnings({"UnusedDeclaration"})
public class WasherController {

    @Autowired
    private WasherService washerService;

    @Autowired
    private WasherRepository washerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;


    @PostMapping(value = "/add-washer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addWasher(@RequestBody Washer washer) {
        washerRepository.save(washer);
        return "New Washer Added !";
    }

    @GetMapping("/get-washer/{name}")
    public Washer getWasherByName(@PathVariable String name) {
        return washerService.findByName(name);
    }

    @GetMapping("/all-washers") // Shows all Washers available in the DB
    public List<Washer> getAllWashers() {
        return washerRepository.findAll();
    }

    @GetMapping("/receive-wash-notifications") //Call to activate Reception of notifications from Customer
    public String notificationTest() throws Exception {
        return washerService.receiveNotification();
    }

    @GetMapping("/washer-choice") //Washer accepting/Rejecting received Wash-Request
    public String acceptWash(@RequestParam("option") Boolean option) {
        return washerService.washerChoice(option);
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (Exception e) {
            throw new Exception("Invalid Username or Password Entered !");
        }
        return jwtUtil.generateToken(authRequest.getUsername());
    }

    @GetMapping("/test-security")
    public String sayHelloToWasher() {
        return "Hey there washer:" + jwtFilter.getLoggedInUserName();
    }

    @GetMapping("/order-accepted")
    public String orderAccepted(){
            washerService.sendNotification("Order-Placed");
        return "Order Placed with washer Partner:" + jwtFilter.getLoggedInUserName();
    }

    @GetMapping("/wash-completed")
    public String completedWash() {
        if(washerService.washRequestFromCustomer().contains("Order-placed")) {
            washerService.sendNotification("wash-completed, proceed for payment...");
        }
        return "Washer Partner served the request: "+ washerService.washRequestFromCustomer();
    }

    @GetMapping("/get-rating")
    public RatingReview takeRating(RatingReview ratingReview){
        return washerService.takeRating(ratingReview);
    }
}
