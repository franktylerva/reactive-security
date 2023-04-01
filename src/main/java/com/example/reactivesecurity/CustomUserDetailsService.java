package com.example.reactivesecurity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class CustomUserDetailsService implements ReactiveUserDetailsService {
    @Override
    public Mono<UserDetails> findByUsername(String username) {

        if(!username.equals("user1"))
            return Mono.empty();

        var user = new CustomUserDetail();
        user.setUserName("user1");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAuthorities(List.of(new SimpleGrantedAuthority("USER")));
        return Mono.just(user);
    }
}
