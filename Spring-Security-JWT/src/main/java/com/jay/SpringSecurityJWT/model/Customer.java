package com.jay.SpringSecurityJWT.model;

import java.util.List;


public class Customer {

    private int id;
    private String name;
    private String password;
    private List<String> address;
    private String carModel;

    public Customer(int id, String name, String password, List<String> address, String carModel) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.address = address;
        this.carModel = carModel;
    }

    public Customer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
