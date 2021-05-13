package com.jay.CWWasherMicroservice.service;

import com.jay.CWWasherMicroservice.model.Washer;
import com.jay.CWWasherMicroservice.repository.WasherRepository;
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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private WasherRepository washerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Washer washer = washerRepository.findByName(username);
        GrantedAuthority authority = new SimpleGrantedAuthority(washer.getRole());
        return new User(washer.getName(), washer.getPassword(), Arrays.asList(authority));
    }
}
