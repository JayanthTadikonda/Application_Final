package com.jay.SpringSecurityJWT.service;

import com.jay.SpringSecurityJWT.model.Customer;
import com.jay.SpringSecurityJWT.model.User;
import com.jay.SpringSecurityJWT.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {


       //User user =  userRepository.findByUsername(userName);
        Customer customer = userRepository.findByUsername(name);
       return new org.springframework.security.core.userdetails.User(customer.getName(),customer.getPassword(), new ArrayList<>());

        //return new User("foo","foo",new ArrayList<>());


    }
}
