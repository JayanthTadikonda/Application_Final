package com.jay.CWAdmin.controller;

import com.jay.CWAdmin.model.*;
import com.jay.CWAdmin.repository.AdminRepository;
import com.jay.CWAdmin.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> listOfPayments() {
        return new ResponseEntity<>(adminService.listOfPayments(), HttpStatus.OK);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> listOfOrders() {
        return new ResponseEntity<>(adminService.listOfOrders(), HttpStatus.OK);
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> listOfCustomers() {
        return new ResponseEntity<>(adminService.listOfCustomers(), HttpStatus.OK);
    }

    @GetMapping("/washers")
    public ResponseEntity<List<Washer>> listOfWashers() {
        return new ResponseEntity<>(adminService.listOfWashers(), HttpStatus.OK);
    }

    @GetMapping("/test")
    public String test() {
        return restTemplate.getForObject("http://payment-microservice/payment/test-payment", String.class);
    }

    @PostMapping("/add-pack")
    public WashPack addNewPack(@RequestBody WashPack pack) {
        return adminService.addNewWashPack(pack);
    }

    @PostMapping("/add-addOn")
    public AddOn addNewAddOn(@RequestBody AddOn add) {
        return adminService.addNewAddOn(add);
    }

    @GetMapping("/get-addOn/{name}")
    public AddOn getAddOn(@PathVariable String name) {
        return adminService.getAddOn(name);
    }

    @GetMapping("/get-pack/{name}")
    public WashPack pack(@PathVariable String name) {
        return adminService.findByName(name);
    }

    @GetMapping("/all-packs")
    public ResponseEntity<List<WashPack>> allPacks() {
        return new ResponseEntity<>(adminRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/all-addOns")
    public ResponseEntity<List<AddOn>> allAddOns() {
        return new ResponseEntity<>(adminService.addOnList(), HttpStatus.OK);
    }

    @PostMapping("/edit-pack/{name}")
    public WashPack editPack(@PathVariable String name, @RequestBody WashPack washPack) {
        return adminService.editWashPack(name, washPack.getAmount(), washPack.getPackName());
    }

    @PostMapping("/edit-addOn/{name}")
    public AddOn editAddOn(@PathVariable String name, @RequestBody AddOn addOn) {
        return adminService.editAddOn(name, addOn.getAmount(), addOn.getAddOnName());
    }

    @PostMapping("/update-leaderboard")
    public WasherLeaderboard addToLeaderboard(@RequestBody WasherLeaderboard washerLeaderboard) {
        return adminService.addNewWasherToLeaderboard(washerLeaderboard);
    }

    @GetMapping("/leaderboard")
    public List<WasherLeaderboard> washerLeaderboard() {
        return adminService.washerLeaderboard();
    }
}
