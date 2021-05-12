package com.jay.MongoDBUserCreation.controller;

import com.jay.MongoDBUserCreation.exceptions.CustomerNotFoundException;
import com.jay.MongoDBUserCreation.filter.JwtFilter;
import com.jay.MongoDBUserCreation.model.*;
import com.jay.MongoDBUserCreation.repository.CustomerRepository;
import com.jay.MongoDBUserCreation.service.CustomerService;
import com.jay.MongoDBUserCreation.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/customer")
@SuppressWarnings({"unused"})
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
    public List<WashPack> getWashPackages() {
        return customerService.getPacks();
    }

    @GetMapping("/schedule-wash/{date}")
    public String scheduleWash(@DateTimeFormat(pattern = "dd.MM.yyyy") @PathVariable LocalDate date) throws Exception {
        String sent = customerService.sendNotification("Requesting for Scheduling wash at: " + date + "By customer: " + jwtFilter.getLoggedInUserName());
        String resp = customerService.receiveNotification();
        return sent;
    }

    @GetMapping("/date/{date}")
    public LocalDate returnDate(@DateTimeFormat(pattern = "dd.MM.yyyy") @PathVariable LocalDate date) {
        return date;
    }

    @GetMapping("/wash-now") //Wash Service Booking
    public String bookWash() throws Exception {

        String sent = customerService.sendNotification("book-wash" + "By customer: " + jwtFilter.getLoggedInUserName());
        String resp = customerService.receiveNotification();

        return sent;
    }

    @GetMapping("/continue") //Proceed to create an ORDER for the Wash
    public OrderResponse placeOrderForAcceptedWashRequest(@RequestParam("pack") String packName, @RequestParam("add-on") String addOnName) throws Exception {
        return customerService.placeOrder(packName, addOnName);
    }

    @GetMapping("/pay") // Pay after the wash is completed.
    public TransactionResponse doPayment(@RequestBody RatingReview ratingReview) throws Exception {
        return customerService.payAfterWash(ratingReview);
    }

    @GetMapping("/rate-washer") // Rate the washer after the wash
    public RatingReview rateWasher(@RequestBody RatingReview ratingReview) {
        return customerService.giveRatingAndReview(ratingReview);
    }

    @PostMapping(value = "/add-customer", consumes = MediaType.APPLICATION_JSON_VALUE) // New Customer Registration
    public Customer addCustomer(@RequestBody Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @GetMapping("/get-customer/{name}") // Get customer with name
    public ResponseEntity<Customer> getCustomerByName(@PathVariable String name) {
        Customer customer = customerService.findByName(name);
        if (customer == null) {
            throw new CustomerNotFoundException("Sorry, Customer with the provided name not found, please provide the name used while registration !");
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @GetMapping("/customer-id/{id}") // Get customer with ID
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new CustomerNotFoundException("Invalid Customer Id, please provide valid ID !");
        }
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
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

    @GetMapping(value = "/test-security") //Testing Logged-In Customer name
    public String sayHelloOnAuthentication() {
        return "Hey there " + jwtFilter.getLoggedInUserName();
    }

    @PostMapping("/authenticate") //Authenticate a Customer (Existing)
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

    @GetMapping("/my-orders")
    public List<Order> myOrders() {
        return customerService.customerOrders(jwtFilter.getLoggedInUserName());
    }

    @GetMapping("/leaderboard")
    public List<WasherLeaderboard> washerLeaderboard() {
        return customerService.washerLeaderboard();
    }
}
