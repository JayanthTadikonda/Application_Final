package com.jay.CWAdmin.service;

import com.jay.CWAdmin.model.*;
import com.jay.CWAdmin.repository.AddOnRepository;
import com.jay.CWAdmin.repository.AdminRepository;
import com.jay.CWAdmin.repository.WasherLeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AddOnRepository addOnRepository;

    @Autowired
    private WasherLeaderboardRepository washerLeaderboardRepository;

    public WashPack addNewWashPack(WashPack pack) {
        WashPack washPack = new WashPack();
        washPack.setPackName(pack.getPackName());
        washPack.setAmount(pack.getAmount());
        adminRepository.save(washPack);
        return washPack;
    }

    public WashPack findByName(String name) {
        return adminRepository.findAll().stream()
                .filter(p -> p.getPackName().contains(name)).findAny().orElse(null);
    }

    public WashPack editWashPack(String packName, double newPrice, String newName) {
        WashPack pack = findByName(packName);
        pack.setPackName(newName);
        pack.setAmount(newPrice);
        return adminRepository.save(pack);
    }

    public List<Payment> listOfPayments() {
        ResponseEntity<List<Payment>> paymentResponse =
                restTemplate.exchange("http://payment-microservice/payment/get-payments-list",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Payment>>() {
                        });
        List<Payment> paymentList = paymentResponse.getBody();
        return paymentList;
    }

    public List<Order> listOfOrders() {
        ResponseEntity<List<Order>> orderResponse =
                restTemplate.exchange("http://order-microservice/order/get-all-orders",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Order>>() {
                        });
        List<Order> orderList = orderResponse.getBody();
        return orderList;
    }

    public List<Customer> listOfCustomers() {
        ResponseEntity<List<Customer>> response =
                restTemplate.exchange("http://customer-microservice/customer/get-all-customers",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Customer>>() {
                        });
        List<Customer> customersList = response.getBody();
        return customersList;
    }

    public List<Washer> listOfWashers() {
        ResponseEntity<List<Washer>> washerResponse =
                restTemplate.exchange("http://washer-microservice/washer/get-all-washers",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Washer>>() {
                        });
        List<Washer> washerList = washerResponse.getBody();
        return washerList;
    }

    public List<WasherLeaderboard> washerLeaderboard() {
        return washerLeaderboardRepository.findAll();
    }

    public AddOn addNewAddOn(AddOn addOn) {
        AddOn add = new AddOn();
        add.setAddOnName(addOn.getAddOnName());
        add.setAmount(addOn.getAmount());
        addOnRepository.save(add);
        return add;
    }

    public AddOn getAddOn(String name) {
        return addOnRepository.findAll().stream().filter(a -> a.getAddOnName().contains(name)).findAny().orElse(null);
    }

    public List<AddOn> addOnList() {
        return addOnRepository.findAll();
    }

    public AddOn editAddOn(String name, double amount, String newAddOnName) {
        AddOn addOn = getAddOn(name);
        addOn.setAddOnName(newAddOnName);
        addOn.setAmount(amount);
        addOnRepository.save(addOn);
        return addOn;
    }

    public WasherLeaderboard addNewWasherToLeaderboard(WasherLeaderboard washerLeaderboard) {
        WasherLeaderboard washerLeaderboard1 = new WasherLeaderboard();
        washerLeaderboard1.setWasherName(washerLeaderboard.getWasherName());
        washerLeaderboard1.setWaterSavedInLiters(washerLeaderboard.getWaterSavedInLiters());
        return washerLeaderboardRepository.save(washerLeaderboard1);
    }

}
