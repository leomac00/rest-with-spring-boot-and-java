package com.leomac00.reststudy.services;

import com.leomac00.reststudy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;


@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    private final Logger logger = Logger.getLogger(UserService.class.getName());
    private final String notFoundMessage = "No user was found for the provided ID!";


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding user by username = " + username);
        var user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException(notFoundMessage);
        }
    }
}
