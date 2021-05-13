package com.jay.MongoDBUserCreation.service;

import com.jay.MongoDBUserCreation.model.Customer;
import com.jay.MongoDBUserCreation.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = customerRepository.findByName(username);
        if(customer != null) {
            GrantedAuthority authority = new SimpleGrantedAuthority(customer.getRole());
            return new User(customer.getName(), customer.getPassword(), Arrays.asList(authority));
        }
        else return new User(null,null,null);
    }
}
