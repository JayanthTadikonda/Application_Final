package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.model.Washer;
import com.jay.CWWasherMicroservice.repository.WasherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private WasherRepository washerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Washer washer = washerRepository.findByName(username);

        return new User(washer.getName(), washer.getPassword(), new ArrayList<>());
    }
}
