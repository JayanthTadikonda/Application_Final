package com.jay.MongoDBUserCreation.controller;

import com.jay.MongoDBUserCreation.filter.JwtFilter;
import com.jay.MongoDBUserCreation.model.*;
import com.jay.MongoDBUserCreation.repository.CustomerRepository;
import com.jay.MongoDBUserCreation.service.CustomerService;
import com.jay.MongoDBUserCreation.util.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/wash-menu")
    public List<WashPack> getWashPackages(){
        return customerService.getPacks();
    }

    @GetMapping("/schedule-wash/{date}")
    public String scheduleWash(@PathVariable LocalDate date)throws Exception {
        String sent = customerService.sendNotification("Requesting for Scheduling wash at: "+ date.format(DateTimeFormatter.BASIC_ISO_DATE) + "By customer: " + jwtFilter.getLoggedInUserName());
        String resp = customerService.receiveNotification();
        return sent;
    }

    @GetMapping("/wash-now") //Wash Service Booking
    public String bookWash() throws Exception {

        String sent = customerService.sendNotification("book-wash" + "By customer: " + jwtFilter.getLoggedInUserName());
        String resp = customerService.receiveNotification();

        return sent;
    }

    @GetMapping("/continue") //Proceed to create an ORDER for the Wash
    public OrderResponse placeOrderForAcceptedWashRequest(@RequestParam("pack") String packName) throws Exception {
        return customerService.placeOrder(packName);
    }

    @GetMapping("/pay") // Pay after the wash is completed.
    public TransactionResponse doPayment() throws Exception {
        return customerService.payAfterWash();
    }

    @PostMapping("/rate-washer")
    public RatingReview rateWasher(@RequestBody RatingReview ratingReview){
        return customerService.giveRatingAndReview(ratingReview);
    }

    @PostMapping(value = "/add-customer", consumes = MediaType.APPLICATION_JSON_VALUE) // New Customer Registration
    public Customer addCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @GetMapping("/get-customer/{name}") // Get customer with name
    public Customer getCustomerByName(@PathVariable String name) {
        return customerService.findByName(name);
    }

    @GetMapping("/customer-id/{id}") // Get customer with ID
    public Customer getCustomerById(@PathVariable int id) {
        return customerRepository.findById(id);
    }

    @GetMapping("/all-customers") //Lists all the customers in the db
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping("/update-profile/{name}") // Update user profile
    public String updateProfile(@PathVariable String name, @RequestBody Customer customer) {
        return customerService.updateProfile(name, customer);
    }

    @GetMapping("/confirmation") //Call to activate Reception of notifications from Customer
    public String notificationTest() throws Exception {
        return customerService.receiveNotification();
    }

    @GetMapping(value = "/test-security")
    public String sayHelloOnAuthentication() {
        return "Hey there " + jwtFilter.getLoggedInUserName();
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
}
