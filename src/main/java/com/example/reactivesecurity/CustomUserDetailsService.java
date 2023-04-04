package com.example.reactivesecurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.List;

public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public Mono<UserDetails> findByUsername(String username) {

        if(!username.equals("user1"))
            return Mono.empty();

        log.debug("CustomUserDetailsService.findByUsername called....");

        var user = new CustomUserDetail();
        user.setUserName("user1");
        user.setPassword("{noop}password");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAuthorities(List.of(new SimpleGrantedAuthority("USER")));
        return Mono.just(user);
    }
}
