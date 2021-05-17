package com.jay.CWAdmin.service;

import com.jay.CWAdmin.model.*;

import java.util.List;

public interface AdminService {

    public WashPack addNewWashPack(WashPack pack);

    public WashPack findByName(String name);

    public WashPack editWashPack(String packName, double newPrice, String newName);

    public List<Payment> listOfPayments();

    public List<Order> listOfOrders();

    public List<Customer> listOfCustomers();

    public List<Washer> listOfWashers();

    public List<WasherLeaderboard> washerLeaderboard();
}
