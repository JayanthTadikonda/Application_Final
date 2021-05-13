package com.jay.CWWasherMicroservice.controller;

import com.jay.CWWasherMicroservice.filter.JwtFilter;
import com.jay.CWWasherMicroservice.model.*;
import com.jay.CWWasherMicroservice.repository.WasherRepository;
import com.jay.CWWasherMicroservice.service.WasherServiceImpl;
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
    private WasherServiceImpl washerServiceImpl;

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
        return washerServiceImpl.findByName(name);
    }

    @GetMapping("/all-washers") // Shows all Washers available in the DB
    public List<Washer> getAllWashers() {
        return washerRepository.findAll();
    }

    @GetMapping("/receive-wash-notifications") //Call to activate Reception of notifications from Customer
    public String notificationTest() throws Exception {
        return washerServiceImpl.receiveNotification();
    }

    @GetMapping("/washer-choice") //Washer accepting/Rejecting received Wash-Request
    public String acceptWash(@RequestParam("option") Boolean option) {
        return washerServiceImpl.washerChoice(option);
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

        String name = jwtFilter.getLoggedInUserName();
        return "Hey there washer:" + name;
    }

    @GetMapping("/order-accepted")
    public String orderAccepted(){
            washerServiceImpl.sendNotification("Order-Placed");
        return "Order Placed with washer Partner:" + jwtFilter.getLoggedInUserName();
    }

    @GetMapping("/wash-completed")
    public String completedWash() {
        if(washerServiceImpl.washRequestFromCustomer().contains("Order-placed")) {
            washerServiceImpl.sendNotification("wash-completed, proceed for payment...");
        }
        return "Washer Partner served the request: "+ washerServiceImpl.washRequestFromCustomer();
    }

    @PostMapping("/get-rating")
    public RatingReview takeRating(@RequestBody RatingReview ratingReview){
        return washerServiceImpl.takeRating(ratingReview);
    }

    @GetMapping("/my-orders")
    public List<Order> myOrders(){
        return washerServiceImpl.washerOrders(jwtFilter.getLoggedInUserName());
    }

    @GetMapping("/washer-leaderboard")
    public List<WasherLeaderboard> leaderboard(){
        return washerServiceImpl.washerLeaderboard();
    }
}
